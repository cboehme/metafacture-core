/*
 * Copyright 2016 Christoph Böhme
 *
 * Licensed under the Apache License, Version 2.0 the "License";
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.culturegraph.mf.morph.api;

/**
 * A receiver of entity events.
 *
 * @author Christoph Böhme
 *
 */
public interface EntityReceiver extends KnowsSourceLocation {

	void receiveEntityStart(final String name, final EntitySource source, int recordCount);

	void receiveEntityEnd(final String name, final EntitySource source, int recordCount);

	void addEntitySource(final EntitySource source);

}
