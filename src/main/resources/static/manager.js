$(function() {

  // display text in the output area
  function showOutput(text) {
    $("#output").text(text);
  }

  // load and display JSON sent by server for /players, using AJAX

  function loadData() {
    $.get("/players")
    .done(function(data) { //When the request is answered, the code inside done() will be executed
      showOutput(JSON.stringify(data, null, 2)); //display exactly the JSON received on the page
    })
    .fail(function( jqXHR, textStatus ) {
      showOutput( "Failed: " + textStatus );
    });
  }

  // handler for when user clicks add person

  function addPlayer() {
    var name = $("#email").val(); //gets data from the web form
    if (name) { //If there is a name string, it passes it to postPlayer()
      postPlayer(name);
    }
    $("#email").val("");
  }

  //sends a JSON object with the user name to the server, using AJAX
  // code to post a new player using AJAX
  // on success, reload and display the updated data from the server

  function postPlayer(userName) {

    //The URL is exactly the same as loadData(). The difference in back-end behavior is based on GET versus POST

    $.post({
      headers: { //It adds a Content-Type header to the request to tell the back-end JSON is coming
          'Content-Type': 'application/json'
      },

      /*It sets the expected return type to text. Otherwise,
      jQuery will get an error trying to parse the empty content returned for the POST as JSON*/

      dataType: "text",
      url: "/players",
      data: JSON.stringify({ "userName": userName })//JSON.stringify() to convert a JavaScript into a valid JSON string
    })
    .done(function( ) {
      showOutput( "Saved -- reloading");
      loadData(); //When the request is finished, it calls loadData() to get the new JSON
    })
    .fail(function( jqXHR, textStatus ) {
      showOutput( "Failed: " + textStatus );
    });
  }

  $("#add_player").on("click", addPlayer);

  loadData();
});

