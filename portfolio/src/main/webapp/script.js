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

function getUserInfo(){
  fetch('/loggedin').then(response => response.json()).then((userInfo) => {
    const commentSectionElement = document.getElementById('comment-section-container');
    commentSectionElement.style.display = "none";
    const emailElement = document.getElementById('email-container');
    emailElement.style.display = "none";
    const loginUrlElement = document.getElementById('login-url');
    loginUrlElement.style.display = "none";
    const logoutUrlElement = document.getElementById('logout-url');
    logoutUrlElement.style.display = "none";
    if(userInfo.isUserLoggedIn){
      commentSectionElement.style.display = "block";
      emailElement.innerHTML = '<p>Logged in as "'+ userInfo.email +'"</p>';
      emailElement.style.display = "block";
      logoutUrlElement.innerHTML = '<p>Logout <a href="'+userInfo.logInUrl+'">here</a>.</p>';
      logoutUrlElement.style.display = "block";
    }else{
      loginUrlElement.innerHTML = '<p>Login <a href= "'+userInfo.logInUrl+'">here</a> to see comments.</p>';
      loginUrlElement.style.display = "block";
    }
  });
  getComment();
}


function createListElement(comment) {
  const liElement = document.createElement('li');
  var name = comment.name;
  var com = comment.comment;
  liElement.innerHTML = `<div id = \\"comments\\"><h2>By ${name}</h2><p>${com}`
  '</div>';

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
