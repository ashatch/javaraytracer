grammar Scene;

// parser rules

// entry point scene()
scene
    : definition+
    ;


definition
    : cameraDefinition
    | lightDefinition
    ;

lightDefinition
    : Light LeftBrace statementList RightBrace
    ;

cameraDefinition
    : Camera LeftBrace statementList RightBrace
    ;

statementList
    : (statement LineEnd)+
    ;

statement
    : vectorDeclaration
    | diameterDeclaration
    | brightnessDeclaration
    | colourDeclaration
    | positionDeclaration
    ;

vectorDeclaration
    : Vector floatList
    ;

diameterDeclaration
    : Diameter Float
    ;

brightnessDeclaration
    : Brightness Float
    ;

colourDeclaration
    : colourKey floatList
    ;

positionDeclaration
    : positionKey floatList
    ;

colourKey
    : 'ambience'
    ;

positionKey
    : 'viewpoint'
    | 'to'
    | 'lookAt'
    ;

floatList
    : Float (Float)*
    ;

Float
    : '-'? '0'..'9'+'.''0'..'9'+
    ;


String
    : '"' ('""'|~'"')* '"'
    ; // quote-quote is an escaped quote

Comment
    : '//' ~ [\r\n]*
    ;

BlockComment
    : '/*' .*? '*/' -> skip
    ;

Separator: ',';
LineEnd : ';';
AssignOp: '=';

LeftBrace: '{';
RightBrace: '}';

Camera: '†';
Light: '⏣';
Vector: '→';
Diameter: 'Ø';
Brightness: 'Γ';

WS: [ \n\t\r]+ -> skip;
