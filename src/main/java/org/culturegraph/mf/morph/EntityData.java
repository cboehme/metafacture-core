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
import org.culturegraph.mf.morph.api.NamedValuePipe;
import org.culturegraph.mf.morph.api.NamedValueReceiver;
import org.culturegraph.mf.morph.api.NamedValueSource;
import org.culturegraph.mf.util.StringUtil;
import org.culturegraph.mf.util.xml.Location;

/**
 * Implementation of the &lt;entity-data&gt; element.
 *
 * @author Christoph Böhme
 *
 */
public final class EntityData implements EntityPipe, NamedValuePipe {

	private final EntityDataBuffer buffer = new EntityDataBuffer();

	private final Deque<String> namePrefix = new LinkedList<String>();
	private int currentRecordCount = -1;

	private Location sourceLocation;
	private String name;
	private boolean unwrap;

	@Override
	public void setEntityReceiver(final EntityReceiver receiver) {
		buffer.setEntityReceiver(receiver);
	}

	@Override
	public void addEntitySource(final EntitySource source) {
		source.setEntityReceiver(this);
	}

	@Override
	public void setNamedValueReceiver(final NamedValueReceiver receiver) {
		buffer.setNamedValueReceiver(receiver);
	}

	@Override
	public void addNamedValueSource(final NamedValueSource source) {
		source.setNamedValueReceiver(this);
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setUnwrap(final boolean unwrap) {
		this.unwrap = unwrap;
	}

	@Override
	public void receiveEntityStart(final String receivedName,
			final EntitySource source, final int recordCount) {

		if (recordCount != currentRecordCount) {
			currentRecordCount = recordCount;
			namePrefix.clear();
			buffer.clear();
		}

		if (namePrefix.isEmpty()) {
			if (!unwrap) {
				buffer.receiveEntityStart(
						StringUtil.fallback(name, receivedName), source,
						recordCount);
			}
		} else {
			buffer.receiveEntityStart(removeNamePrefix(receivedName),
					source, recordCount);
		}
		namePrefix.push(receivedName);
	}

	@Override
	public void receiveEntityEnd(final String receivedName,
			final EntitySource source, final int recordCount) {

		namePrefix.pop();
		if (namePrefix.isEmpty()) {
			if (!unwrap) {
				buffer.receiveEntityEnd(
						StringUtil.fallback(name, receivedName), source,
						recordCount);
			}
			buffer.replay();
		} else {
			buffer.receiveEntityEnd(removeNamePrefix(receivedName),
					source, recordCount);
		}
	}

	@Override
	public void receive(final String name, final String value,
			final NamedValueSource source, final int recordCount,
			final int entityCount) {

		if (namePrefix.peek().equals(name)) {
			return;
		}
		buffer.receive(removeNamePrefix(name), value, source,
				recordCount, entityCount);
	}

	@Override
	public void setSourceLocation(final Location sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	@Override
	public Location getSourceLocation() {
		return sourceLocation;
	}

	@Override
	public String toString() {
		return name;
	}

	private String removeNamePrefix(final String name) {
		final int prefixLength = namePrefix.peek().length() + 1;
		return name.substring(prefixLength);
	}

}
