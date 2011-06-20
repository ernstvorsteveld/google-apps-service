/**
 * Copyright (C) 2009-2010 Wilfred Springer
 * Changed by Ernst Vorsteveld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package nl.iwelcome.connector.google;

/**
 * A collection of operations for interacting Google Reader.
 */
public interface GoogleAppsOperations {

    <T> T doWithCallback(ReaderCallback<T> callback);

    /**
     * Retrieves a token to be included in editing type of calls.
     */
    String getToken();

}
