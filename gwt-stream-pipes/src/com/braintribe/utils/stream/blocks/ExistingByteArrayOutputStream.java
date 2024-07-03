// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
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
package com.braintribe.utils.stream.blocks;

import java.io.IOException;
import java.io.OutputStream;

class ExistingByteArrayOutputStream extends OutputStream {
	private final byte[] memory;
	private long pos;

	public ExistingByteArrayOutputStream(byte[] memory) {
		this.memory = memory;
	}

	@Override
	public void write(int b) throws IOException {
		memory[(int) pos] = (byte) b;
		pos ++;
	}
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		System.arraycopy(b, off, memory, (int) pos, len);
		pos += len;
	}
	@Override
	public void write(byte[] b) throws IOException {
		this.write(b, 0, b.length);
		pos += b.length;
	}
}