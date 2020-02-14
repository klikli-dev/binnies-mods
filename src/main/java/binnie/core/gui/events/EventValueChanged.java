package binnie.core.gui.events;

import binnie.core.gui.IWidget;

import javax.annotation.Nullable;

public class EventValueChanged<T> extends Event {
	@Nullable
	public T value;

	public EventValueChanged(final IWidget origin, @Nullable final T value) {
		super(origin);
		this.value = value;
	}

	@Nullable
	public T getValue() {
		return this.value;
	}

	public abstract static class Handler extends EventHandler<EventValueChanged> {
		public Handler() {
			super(EventValueChanged.class);
		}
	}
}
