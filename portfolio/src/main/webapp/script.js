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

function getComment() {
  fetch('/data').then(response => response.json()).then((comments) => {
    const statsListElement = document.getElementById('comment-container');
    statsListElement.innerHTML = '';
    for (i = 0; i < comments.length; i++) {
        statsListElement.appendChild(createListElement(comments[i]));
    };
  });

}

function createListElement(comment) {
  const liElement = document.createElement('li');
  liElement.innerText = comment.comment;

  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = 'Delete';
  deleteButtonElement.addEventListener('click', () => {
    deleteComment(comment);

    // Remove the task from the DOM.
    liElement.remove();
  });

  liElement.appendChild(deleteButtonElement);
  return liElement;
}

function deleteComment(comment){
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete-comment', {method: 'POST', body: params});
}
