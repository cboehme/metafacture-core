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
 * A sender of entity events.
 *
 * @author Christoph Böhme
 *
 */
public interface EntitySource extends KnowsSourceLocation {

	/**
	 * Connects a source of entities to a receiver of entities.
	 *
	 * Users should not call this method to connect sources and
	 * receivers but rather call
	 * {@link EntityReceiver#addEntitySource}.
	 */
	void setEntityReceiver(EntityReceiver receiver);

}
