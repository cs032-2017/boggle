// TODO: Declare a class called Position that takes in two fields: row and col




// INPUT: The Position of the previously clicked tile and the Position of the current tile
// OUTPUT: boolean, true if the currPosition is a valid neighbor of lastPosition, including if
//         currPosition is the same as lastPosition; false otherwise
function isValidClick(lastPosition, currPosition) {
    
    // TODO: Fill this out!

}

// Helper function for parsing coordinates from id
function getCoordinateArray(id) {
    return id.split('-').map(x => parseInt(x));
}

// Waits for DOM to load before running
$(document).ready(() => {
    // Variables to keep track of game state
    let currWord = '',
        totalScore = 0,
        guessList = [],
        positions = [];

    // TODO: Use jQuery to get reference to the HTML element with an id of "score".
    // We'll use this later to display the score.
    const $scoreText = _______;

    // TODO: use jQuery to get reference to the HTML element with an id of "message".
    // We'll use this later to inform the player whether the guess was correct.
    const $message = _______;

    // TODO: Use jQuery to get reference to the HTML <ul> element with an id of "guesses".
    // We'll append guesses to this element to display them to the user.
    const $guesses = _______;

    // TODO: Use jQuery to get a reference to all the "td" elements on the board.
    // A 'td' element represents a letter on the board
    const elements = _______;

    // Binding click handlers to each td element (i.e. letter)
    for (let i = 0, len = elements.length; i < len; i++) {
        $(elements[i]).on('click', event => {
            const $letter = $(event.currentTarget);

            // Retrieving id from currently clicked letter
            const id = $letter.attr('id');

            // Parsing id to retrieve current position
            const coordinates = getCoordinateArray(id);

            // TODO: Initialize a new Position object using coordinates.
            const currPosition = _______;

            // Getting previous position from positions array
            const lastPosition = positions[positions.length-1];

            // If this is the first letter to be clicked, or if it is a "valid" click...
            if (positions.length === 0 || isValidClick(lastPosition, currPosition)) {
                if ($letter.hasClass('selected')) {
                    // If current position has already been selected and it was the last
                    // position to be selected, deselect it
                    if (currPosition.row === lastPosition.row && 
                        currPosition.col === lastPosition.col) {

                        // Fix internal state representation ("undo-ing" last selection)
                        currWord = currWord.substring(0, currWord.length - 1);
                        positions.pop();

                        $letter.toggleClass('selected');
                    }
                } else {
                    $letter.toggleClass('selected');

                    // Update internal state representation 
                    currWord += $letter.html().trim();
                    positions.push(currPosition);
                }
            }
        });
    };

    // Listen for keypress events. If player presses the Enter key, the game validates the current word.
    $(document).keypress(event => {
        // 13 is the key code for the Enter key
        if (event.which == 13 && currWord.length > 0) {
            if (!guessList.includes(currWord)) {
                // Adds currWord to guessList array
                guessList.push(currWord);

                // Update the guesses input element of our hidden form
                $('input[name=guesses]').val(guessList.join(" "));

                // TODO: Use jQuery .append() to append currWord to $guesses. This should update
                //       the DOM by adding a new list item.

                // HINT: You'll need to create string that wraps currWord in "li" (list item) tags!
                //       Check online for examples of jQuery's .append() function and documentation.


                // TODO: Build the Javascript object that contains data for the POST request.
                const postParameters = _______;

                // TODO: Make a POST request to the "/validate" endpoint with the word information
                $.post(_______, postParameters, responseJSON => {

                    // TODO: Parse the JSON response into a JavaScript object.
                    const responseObject = _______;
                    
                    // TODO: Fill in the following conditionals for updating score if the word was valid.

                    // If the word was valid....
                    if (_______) {

                        // TODO:
                        // 1. Update totalScore
                        // 2. Update the text in $scoreText with the updated score
                        // 3. Update the text in $message with a happy message :D

                        // HINT: Check out jQuery's .html() function to update the text! Again, there's
                        //       lots of examples and documentation online.

                    } else { // If the word was not valid...

                        // TODO: Update the text in $message with a sad message :(

                    }
                });
            }

            // Select all elements with the "selected" class and remove the "selected" class
            $('.selected').removeClass('selected');

            // Resetting internal state representation
            currWord = '';
            positions = [];
        }
    });
});
