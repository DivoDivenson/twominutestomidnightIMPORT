all:
	mono ./Coco.exe ./Taste.ATG -namespace Taste
	gmcs Taste.cs Scanner.cs Parser.cs SymTab.cs CodeGen.cs
	
run:
	mono ./Taste.exe ./Test.TAS
