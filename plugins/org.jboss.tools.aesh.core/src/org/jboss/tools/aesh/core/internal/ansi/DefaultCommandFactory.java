/**
 * Copyright (c) Red Hat, Inc., contributors and others 2013 - 2014. All rights reserved
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.tools.aesh.core.internal.ansi;

public class DefaultCommandFactory implements CommandFactory {
	
	public static final DefaultCommandFactory INSTANCE = new DefaultCommandFactory();
	
	private DefaultCommandFactory() {}
	
	public Command create(String controlSequence) {
		int last = controlSequence.length() - 1;
		char c = controlSequence.charAt(last);
		CommandType type = CommandType.fromCharacter(c);
		if (type == null) {
			return null;
		} else {
			String arguments = controlSequence.substring(2, last);
			return create(type, arguments);
		}
	}
	
	private static Command create(
			CommandType type, 
			String arguments) {
		switch (type) {
		case CURSOR_UP: return new CursorUp(arguments);
		case CURSOR_DOWN: return new CursorDown(arguments);
		case CURSOR_FORWARD: return new CursorForward(arguments);
		case CURSOR_BACK: return new CursorBack(arguments);
		case CURSOR_NEXT_LINE: return new CursorNextLine(arguments);
		case CURSOR_PREVIOUS_LINE: return new CursorPreviousLine(arguments);
		case CURSOR_HORIZONTAL_ABSOLUTE: return new CursorHorizontalAbsolute(arguments);
		case CURSOR_POSITION: return new CursorPosition(arguments);
		case ERASE_DATA: return new EraseData(arguments);
		case ERASE_IN_LINE: return new EraseInLine(arguments);
		case SCROLL_UP: return new ScrollUp(arguments);
		case SCROLL_DOWN: return new ScrollDown(arguments);
		case HORIZONTAL_AND_VERTICAL_POSITION: return new HorizontalAndVerticalPosition(arguments);
		case SELECT_GRAPHIC_RENDITION: return new SelectGraphicRendition(arguments);
		case DEVICE_STATUS_REPORT: return new DeviceStatusReport(arguments);
		case SAVE_CURSOR_POSITION: return new SaveCursorPosition(arguments);
		case RESTORE_CURSOR_POSITION: return new RestoreCursorPosition(arguments);
		case HIDE_CURSOR: return new HideCursor(arguments);
		case SHOW_CURSOR: return new ShowCursor(arguments);
		default: throw new RuntimeException("Unknown Ansi Control Sequence");
		}
		
	}

}
