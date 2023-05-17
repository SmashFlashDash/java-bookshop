package com.example.MyBookShopApp.data.book.file;

public enum BookFileTypeEnumNoTable {

    PDF(".pdf"), EPUB(".epub"), FB2(".fb2");

    private final String fileExtensionString;

    BookFileTypeEnumNoTable(String fileExtensionString) {
        this.fileExtensionString = fileExtensionString;
    }

    public static String getExtensionStringByTypeId(Integer typeId){
        switch (typeId){
            case 1: return BookFileTypeEnumNoTable.PDF.fileExtensionString;
            case 2: return BookFileTypeEnumNoTable.EPUB.fileExtensionString;
            case 3: return BookFileTypeEnumNoTable.FB2.fileExtensionString;
            default: return "";
        }
    }
}
