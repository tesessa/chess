# My notes

Bishop moves test, Bishop capture enemy:
To get this correct, you want to use a chessboard, you've set up a chessboard and you use the chessboard getPiece(ChessPosition) method to be able to check if there is a piece in that spot right there. That method will return a ChessPiece and you check for PieceType and team color to see if you're able to go there. If it is the same color you are blocked, if it is not you can go there but the loop needs to break after that.

so example:
ChessBoard insertName = new ChessBoard();
if(insertName.getPiece(row,col).getPieceType()!=null) {
	you allow it to add value
}

but if(insertName.getPiece(row,col).getTeamColor() != pieceYoureTryingToMove.getTeamColor()) {
//	you allow it to add value and then break, if it is equal to the piece you're trying to move you need to break the for loop

}
