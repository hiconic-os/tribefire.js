// ============================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.model.processing.session.api.resource;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

import com.braintribe.model.generic.GmCoreApiInteropNamespaces;
import com.braintribe.model.generic.session.OutputStreamProvider;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.resourceapi.stream.BinaryRetrievalResponse;
import com.braintribe.model.resourceapi.stream.condition.StreamCondition;
import com.braintribe.model.resourceapi.stream.range.StreamRange;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * <p>
 * Builder for retrieving the binary data associated with {@link Resource}.
 * 
 * @author dirk.scheffler
 */
@JsType(namespace=GmCoreApiInteropNamespaces.resources)
@SuppressWarnings("unusable-by-js")
public interface ResourceRetrieveBuilder {

	/**
	 * Sets an optional {@link StreamRange}.
	 * <p>
	 * 
	 * If provided, the resulting stream should only include the provided range.
	 * 
	 * @param range The range of the requested stream.
	 * @return The {@link ResourceRetrieveBuilder} object.
	 */
	ResourceRetrieveBuilder range(StreamRange range);
	
	/**
	 * <p>
	 * Sets an optional {@link StreamCondition}.
	 * 
	 * <p>
	 * When the provided, the stream operations are only effective when the condition matches.
	 * 
	 * <p>
	 * For stream operations working on the basis of a {@link OutputStream}, no data is written to the stream in case of
	 * non-matching condition.
	 * 
	 * <p>
	 * For stream operations returning {@link InputStream}(s), {@code null} is returned in case of non-matching
	 * condition.
	 * 
	 * @param condition
	 *            The {@link StreamCondition} to drive the stream operations.
	 */
	ResourceRetrieveBuilder condition(StreamCondition condition);

	/**
	 * <p>
	 * Sets an optional {@link Consumer} to be notified about a {@link BinaryRetrievalResponse} before data is written
	 * to the provided {@link OutputStream}.
	 * 
	 * @param consumer
	 *            The {@link Consumer} to be notified about a {@link BinaryRetrievalResponse} before data is written to
	 *            the provided {@link OutputStream}.
	 */
	ResourceRetrieveBuilder onResponse(Consumer<BinaryRetrievalResponse> consumer);

	/**
	 * <p>
	 * Streams the binary data to the given {@link OutputStream}.
	 * 
	 * <p>
	 * If a non-matching {@link StreamCondition} was given (see {@link #condition(StreamCondition)}), the
	 * {@link OutputStream} is left untouched.
	 * 
	 * @param outputStream
	 *            The {@link OutputStream} to which the retrieved binary data can be written.
	 * @throws java.io.UncheckedIOException
	 *             If the IO operation fails.
	 */
	@JsIgnore
	void stream(OutputStream outputStream);

	/**
	 * <p>
	 * Streams the binary data to the {@link OutputStream} provided by the given {@link OutputStreamProvider}.
	 * 
	 * <p>
	 * If a non-matching {@link StreamCondition} was given (see {@link #condition(StreamCondition)}), the
	 * {@link OutputStream} is left untouched.
	 * 
	 * @param outputStreamProvider
	 *            Provides the {@link OutputStream} to which the retrieved binary data can be written.
	 * @throws java.io.UncheckedIOException
	 *             If the IO operation fails.
	 */
	@JsIgnore
	void stream(OutputStreamProvider outputStreamProvider);

	/**
	 * <p>
	 * Returns a {@link InputStream} from which the binary data can be read.
	 * 
	 * <p>
	 * If a non-matching {@link StreamCondition} was given (see {@link #condition(StreamCondition)}), this method
	 * returns {@code null}.
	 * 
	 * @return The {@link InputStream} from which the binary data can be read.
	 * @throws java.io.UncheckedIOException
	 *             If the IO operation fails.
	 */
	@JsIgnore
	InputStream stream();

}
