const MESSAGE_TYPE = {
  CONNECT: 0,
  SCORE: 1,
  UPDATE: 2
};

let conn;
let myId = -1;

// Setup the WebSocket connection for live updating of scores.
const setup_live_scores = () => {
  // TODO Create the WebSocket connection and assign it to `conn`

  conn.onerror = err => {
    console.log('Connection error:', err);
  };

  conn.onmessage = msg => {
    const data = JSON.parse(msg.data);
    switch (data.type) {
      default:
        console.log('Unknown message type!', data.type);
        break;
      case MESSAGE_TYPE.CONNECT:
        // TODO Assign myId
        break;
      case MESSAGE_TYPE.UPDATE:
        // TODO Update the relevant row or add a new row to the scores table
        break;
    }
  };
}

// Should be called when a user makes a new guess.
// `guesses` should be the array of guesses the user has made so far.
const new_guess = guesses => {
  // TODO Send a SCORE message to the server using `conn`
}
