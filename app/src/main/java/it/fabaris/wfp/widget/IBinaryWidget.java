/*******************************************************************************
 * Copyright (c) 2012 Fabaris SRL.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Fabaris SRL - initial API and implementation
 ******************************************************************************/
/*
 * Copyright (C) 2009 University of Washington
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package it.fabaris.wfp.widget;

/**
 * Interface implemented by widgets that need binary data.
 * 
 * @author Carl Hartung (carlhartung@gmail.com)
 */
public interface IBinaryWidget {
    public void setBinaryData(Object answer);
    public boolean isWaitingForBinaryData();
}

/*TODO carlhartung:  we might want to move this into the QuestionWidget abstract class? 
 * 
 */
