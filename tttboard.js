//tttBoard - 2-dimensional tic-tac-toe board
//Param size - int -  specifies dimension of board ie. size X size
function tttBoard(size, player0, player1) {
  if (typeof size !== "number" || size <= 0) {
    throw "Invalid param size, must have positive integer to construct board.";
  }
  if (typeof player0 !== "string") {
    throw "Invalid param player0, must have string as name for player0.";
  }
  if (typeof player1 !== "string") {
    throw "Invalid param player1, must have string as name for player1.";
  }
  this.size = size;
  this.player0 = player0;
  this.player1 = player1;
  this.board = new Array();
  for (var i=0; i<size; i++) {
    this.board[i] = new Array();
    for (var j=0; j<size; j++) {
      this.board[i][j] = "";
    }
  }
  
  function place(player, x, y) {
    if ((player !== 0) && (player !== 1)) {
      throw "Invalid param player, must have 0 or 1 for player0 or player1.";
    }
    if (typeof x !== "number") {
      throw "Invalid param x, must have integer to place marker.";
    }
    if (typeof y !== "number") {
      throw "Invalid param y, must have integer to place marker.";
    }
    if (this.board[x][y] !== "") {
      throw "Invalid params x and y. (x,y) is already occupied.";
    }
    if (player === 0) {this.board[x][y] = this.player0;}
    else {this.board[x][y] = this.player1;}
    this.check();
  }

  //check for a winner or a draw scenario
  //size consecutive 
  function check() {
    var won = false;
    //check each row for a winner
    //if all rows are full, draw unless someone won a column or diagonal
    var rowsFull = new Array();
    for (var i=0; i<this.size; i++) {
      var first = this.board[i][0];
      var same = true;
      rowsFull[i] = true;
      for (var j=1; j<this.size; j++) {
        if (this.board[i][j] === "") { //row not full, row not won
          same = false;
          rowsFull[i] = false;
        }
        if (this.board[i][j] !== first) { //row not won
          same = false;
        }
      }
      if (same) { //somebody won the row
        winner(first);
        won = true;
      }
    }
    
  }
}
