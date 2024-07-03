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
package com.braintribe.utils.io;

import java.io.File;

/**
 * {@link WriterBuilder} extension that writes data to a {@link File}.
 * 
 * @author peter.gazdik
 */
public interface FileWriterBuilder extends WriterBuilder<File> {

	/** If set to {@code true}, the writer will append the data to the end of the file if the file already exists. */
	FileWriterBuilder append(boolean append);

}
