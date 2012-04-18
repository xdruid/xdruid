/*
 * Copyright (C) 2012, Pavle Jonoski
 * 
 * This file is part of xdruid.
 *
 *   xdruid is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   xdruid is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with xdruid.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xdruid.ui;

/**
 * Screen is a logical whole that wraps a domain object, a layout and behavior
 * specific only to the components displayed on the current layout. <br>
 * The screen behaves as a single component and as such has its own life-cycle.
 * Each screen starts in state <code>CREATED</code>. This happens immediately
 * after the Screen object is instanced. If the Screen is in
 * <code>CREATED</code> state the availability and the resolution of the layouts
 * is guaranteed.<br>
 * The {@link Dispatcher} will initialize the Screen when possible with the
 * user-specified domain object. In that case
 * {@link Screen#initializing(Object)} is called.
 * 
 * 
 * @author Pavle Jonoski
 * 
 */
public interface Screen {

	/**
	 * 
	 * @author Pavle Jonoski
	 * 
	 */
	public enum State {
		/**
		 * 
		 */
		CREATED,
		/**
		 * 
		 */
		INITIALIZED, 
		/**
		 * 
		 */
		VISIBLE,
		/**
		 * 
		 */
		HIDDEN, 
		/**
		 * 
		 */
		DESTROYED
	}

	public void initializing(Object domainObject) throws Exception;

	public void destroying() throws Exception;

	public void reloading(Object domainObject) throws Exception;

	public void screenVisible() throws Exception;

	public void screenHidden() throws Exception;

	public Object getValue();

	public String getCurrenLayoutName();

	public State getState();

	public boolean isInitialized();

	public boolean isVisible();

	public boolean isHidden();

	public boolean isDestroyed();

	public String getName();

}
