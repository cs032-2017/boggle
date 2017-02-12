<form method="POST" action="/results">
  <input type="hidden" name="board" value="${board.toString(',')}">
  <input type="hidden" name="guesses">

  <p id="message"></p>
  <p>Score: <span id="score">0</span></p>
  
  <div>
  	<p>Guessed words:</p>
  	<ul id="guesses"></ul>
  </div>
  
  <input type="submit" value="End Game">
</form>
