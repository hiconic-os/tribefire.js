/*
 * [The "BSD license"]
 *  Copyright (c) 2013 Terence Parr
 *  Copyright (c) 2013 Sam Harwell
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.antlr.v4.runtime.atn;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.dfa.DFAState;

import java.util.BitSet;

/**
 * @since 4.3
 */
public class ProfilingATNSimulator extends ParserATNSimulator {
	protected final DecisionInfo[] decisions;
	protected int numDecisions;

	protected int _sllStopIndex;
	protected int _llStopIndex;

	protected int currentDecision;
	protected DFAState currentState;

 	/** At the point of LL failover, we record how SLL would resolve the conflict so that
	 *  we can determine whether or not a decision / input pair is context-sensitive.
	 *  If LL gives a different result than SLL's predicted alternative, we have a
	 *  context sensitivity for sure. The converse is not necessarily true, however.
	 *  It's possible that after conflict resolution chooses minimum alternatives,
	 *  SLL could get the same answer as LL. Regardless of whether or not the result indicates
	 *  an ambiguity, it is not treated as a context sensitivity because LL prediction
	 *  was not required in order to produce a correct prediction for this decision and input sequence.
	 *  It may in fact still be a context sensitivity but we don't know by looking at the
	 *  minimum alternatives for the current input.
 	 */
	protected int conflictingAltResolvedBySLL;

	public ProfilingATNSimulator(Parser parser) {
		super(parser,
				parser.getInterpreter().atn,
				parser.getInterpreter().decisionToDFA,
				parser.getInterpreter().sharedContextCache);
		numDecisions = atn.decisionToState.size();
		decisions = new DecisionInfo[numDecisions];
		for (int i=0; i<numDecisions; i++) {
			decisions[i] = new DecisionInfo(i);
		}
	}

	@Override
	public int adaptivePredict(TokenStream input, int decision, ParserRuleContext outerContext) {
		try {
			this._sllStopIndex = -1;
			this._llStopIndex = -1;
			this.currentDecision = decision;
			// Originally nanoTime but changed 4 GWT to milliSeconds
			long start = System.currentTimeMillis(); // expensive but useful info
			int alt = super.adaptivePredict(input, decision, outerContext);
			long stop = System.currentTimeMillis();
			decisions[decision].timeInPrediction += (stop-start);
			decisions[decision].invocations++;

			int SLL_k = _sllStopIndex - _startIndex + 1;
			decisions[decision].SLL_TotalLook += SLL_k;
			decisions[decision].SLL_MinLook = decisions[decision].SLL_MinLook==0 ? SLL_k : Math.min(decisions[decision].SLL_MinLook, SLL_k);
			if ( SLL_k > decisions[decision].SLL_MaxLook ) {
				decisions[decision].SLL_MaxLook = SLL_k;
				decisions[decision].SLL_MaxLookEvent =
						new LookaheadEventInfo(decision, null, input, _startIndex, _sllStopIndex, false);
			}

			if (_llStopIndex >= 0) {
				int LL_k = _llStopIndex - _startIndex + 1;
				decisions[decision].LL_TotalLook += LL_k;
				decisions[decision].LL_MinLook = decisions[decision].LL_MinLook==0 ? LL_k : Math.min(decisions[decision].LL_MinLook, LL_k);
				if ( LL_k > decisions[decision].LL_MaxLook ) {
					decisions[decision].LL_MaxLook = LL_k;
					decisions[decision].LL_MaxLookEvent =
							new LookaheadEventInfo(decision, null, input, _startIndex, _llStopIndex, true);
				}
			}

			return alt;
		}
		finally {
			this.currentDecision = -1;
		}
	}

	@Override
	protected DFAState getExistingTargetState(DFAState previousD, int t) {
		// this method is called after each time the input position advances
		// during SLL prediction
		_sllStopIndex = _input.index();

		DFAState existingTargetState = super.getExistingTargetState(previousD, t);
		if ( existingTargetState!=null ) {
			decisions[currentDecision].SLL_DFATransitions++; // count only if we transition over a DFA state
			if ( existingTargetState==ERROR ) {
				decisions[currentDecision].errors.add(
						new ErrorInfo(currentDecision, previousD.configs, _input, _startIndex, _sllStopIndex, false)
				);
			}
		}

		currentState = existingTargetState;
		return existingTargetState;
	}

	@Override
	protected DFAState computeTargetState(DFA dfa, DFAState previousD, int t) {
		DFAState state = super.computeTargetState(dfa, previousD, t);
		currentState = state;
		return state;
	}

	@Override
	protected ATNConfigSet computeReachSet(ATNConfigSet closure, int t, boolean fullCtx) {
		if (fullCtx) {
			// this method is called after each time the input position advances
			// during full context prediction
			_llStopIndex = _input.index();
		}

		ATNConfigSet reachConfigs = super.computeReachSet(closure, t, fullCtx);
		if (fullCtx) {
			decisions[currentDecision].LL_ATNTransitions++; // count computation even if error
			if ( reachConfigs!=null ) {
			}
			else { // no reach on current lookahead symbol. ERROR.
				// TODO: does not handle delayed errors per getSynValidOrSemInvalidAltThatFinishedDecisionEntryRule()
				decisions[currentDecision].errors.add(
					new ErrorInfo(currentDecision, closure, _input, _startIndex, _llStopIndex, true)
				);
			}
		}
		else {
			decisions[currentDecision].SLL_ATNTransitions++;
			if ( reachConfigs!=null ) {
			}
			else { // no reach on current lookahead symbol. ERROR.
				decisions[currentDecision].errors.add(
					new ErrorInfo(currentDecision, closure, _input, _startIndex, _sllStopIndex, false)
				);
			}
		}
		return reachConfigs;
	}

	@Override
	protected boolean evalSemanticContext(SemanticContext pred, ParserRuleContext parserCallStack, int alt, boolean fullCtx) {
		boolean result = super.evalSemanticContext(pred, parserCallStack, alt, fullCtx);
		if (!(pred instanceof SemanticContext.PrecedencePredicate)) {
			boolean fullContext = _llStopIndex >= 0;
			int stopIndex = fullContext ? _llStopIndex : _sllStopIndex;
			decisions[currentDecision].predicateEvals.add(
				new PredicateEvalInfo(currentDecision, _input, _startIndex, stopIndex, pred, result, alt, fullCtx)
			);
		}

		return result;
	}

	@Override
	protected void reportAttemptingFullContext(DFA dfa, BitSet conflictingAlts, ATNConfigSet configs, int startIndex, int stopIndex) {
		if ( conflictingAlts!=null ) {
			conflictingAltResolvedBySLL = conflictingAlts.nextSetBit(0);
		}
		else {
			conflictingAltResolvedBySLL = configs.getAlts().nextSetBit(0);
		}
		decisions[currentDecision].LL_Fallback++;
		super.reportAttemptingFullContext(dfa, conflictingAlts, configs, startIndex, stopIndex);
	}

	@Override
	protected void reportContextSensitivity(DFA dfa, int prediction, ATNConfigSet configs, int startIndex, int stopIndex) {
		if ( prediction != conflictingAltResolvedBySLL ) {
			decisions[currentDecision].contextSensitivities.add(
					new ContextSensitivityInfo(currentDecision, configs, _input, startIndex, stopIndex)
			);
		}
		super.reportContextSensitivity(dfa, prediction, configs, startIndex, stopIndex);
	}

	@Override
	protected void reportAmbiguity(DFA dfa, DFAState D, int startIndex, int stopIndex, boolean exact,
								   BitSet ambigAlts, ATNConfigSet configs)
	{
		int prediction;
		if ( ambigAlts!=null ) {
			prediction = ambigAlts.nextSetBit(0);
		}
		else {
			prediction = configs.getAlts().nextSetBit(0);
		}
		if ( configs.fullCtx && prediction != conflictingAltResolvedBySLL ) {
			// Even though this is an ambiguity we are reporting, we can
			// still detect some context sensitivities.  Both SLL and LL
			// are showing a conflict, hence an ambiguity, but if they resolve
			// to different minimum alternatives we have also identified a
			// context sensitivity.
			decisions[currentDecision].contextSensitivities.add(
					new ContextSensitivityInfo(currentDecision, configs, _input, startIndex, stopIndex)
			);
		}
		decisions[currentDecision].ambiguities.add(
			new AmbiguityInfo(currentDecision, configs, _input, startIndex, stopIndex, configs.fullCtx)
		);
		super.reportAmbiguity(dfa, D, startIndex, stopIndex, exact, ambigAlts, configs);
	}

	// ---------------------------------------------------------------------

	public DecisionInfo[] getDecisionInfo() {
		return decisions;
	}
}
