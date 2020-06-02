// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

function addRandomFact(){
    const facts =
        ['My favoirite show is Everybody hates Chris','I have 2 siblings',
        'I used to live in Germany as a baby','My favorite video game is God of war'];

    const fact = facts[Math.floor(Math.random() * facts.length)];

    const factContainer = document.getElementById('fact-container');
    factContainer.innerText = fact;
}

function getQuote() {
  fetch('/data').then(response => response.json()).then((quote) => {
    const statsListElement = document.getElementById('quote-container');
    statsListElement.innerHTML = '';
    statsListElement.appendChild(
        createListElement(quote[0]));
    statsListElement.appendChild(
        createListElement(quote[1]));
    statsListElement.appendChild(
        createListElement(quote[2]));
  });


}
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
