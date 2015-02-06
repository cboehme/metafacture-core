/*
 *  Copyright 2015 Christoph Böhme
 *
 *  Licensed under the Apache License, Version 2.0 the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.culturegraph.mf.morph;

import java.util.Deque;
import java.util.LinkedList;

import org.culturegraph.mf.morph.api.EntityPipe;
import org.culturegraph.mf.morph.api.EntityReceiver;
import org.culturegraph.mf.morph.api.EntitySource;
import org.culturegraph.mf.morph.api.NamedValueSource;
import org.culturegraph.mf.morph.api.helpers.AbstractNamedValuePipe;

/**
 * @author christoph
 *
 */
public final class EntityDataBuffer extends AbstractNamedValuePipe implements EntityPipe {

	private final Deque<Event> buffer = new LinkedList<Event>();
	private EntityReceiver receiver;

	@Override
	public void setEntityReceiver(final EntityReceiver receiver) {
		this.receiver = receiver;
	}

	@Override
	public void addEntitySource(final EntitySource source) {
		source.setEntityReceiver(this);
	}

	@Override
	public void receiveEntityStart(final String name, final EntitySource source,
			final int recordCount) {

		buffer.add(new Event() {
			@Override
			public void replay() {
				receiver.receiveEntityStart(name, source, recordCount);
			}
		});
	}

	@Override
	public void receiveEntityEnd(final String name, final EntitySource source,
			final int recordCount) {

		buffer.add(new Event() {
			@Override
			public void replay() {
				receiver.receiveEntityEnd(name, source, recordCount);
			}
		});
	}

	@Override
	public void receive(final String name, final String value, final NamedValueSource source,
			final int recordCount, final int entityCount) {

		buffer.add(new Event() {
			@Override
			public void replay() {
				getNamedValueReceiver().receive(name, value, source, recordCount, entityCount);
			}
		});
	}

	public void replay() {
		for (final Event event : buffer) {
			event.replay();
		}
		buffer.clear();
	}

	public void clear() {
		buffer.clear();
	}

	private interface Event {
		void replay();
	}

}
