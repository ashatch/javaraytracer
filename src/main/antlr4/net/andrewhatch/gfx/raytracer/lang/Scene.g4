grammar Scene;

// parser rules

// entry point scene()
scene
    : definition+
    ;


definition
    : cameraDefinition
    | lightDefinition
    | sphereDefinition
    | planeDefinition
    | opticsDefinition
    ;

lightDefinition
    : Light LeftBrace statementList RightBrace
    ;

cameraDefinition
    : Camera LeftBrace cameraStatementList RightBrace
    ;

sphereDefinition
    : Sphere LeftBrace statementList RightBrace
    ;

planeDefinition
    : Plane LeftBrace statementList RightBrace
    ;

opticsDefinition
    : OpticsDef Identifier LeftBrace opticsStatementList RightBrace
    ;

statementList
    : (statement LineEnd)+
    ;

opticsStatementList
    : (opticsStatement LineEnd)+
    ;

cameraStatementList
    : (cameraStatement LineEnd)+
    ;

cameraStatement
    : cameraParameterDeclaration
    | ambienceDeclaration
    | vectorDeclaration
    ;

statement
    : vectorDeclaration
    | diameterDeclaration
    | brightnessDeclaration
    | lightAssignment
    ;

vectorDeclaration
    : Vector Float Float Float
    ;

diameterDeclaration
    : Diameter Float
    ;

brightnessDeclaration
    : Brightness Float
    ;

cameraParameterDeclaration
    : cameraParameterKey floatList
    ;

lightAssignment
    : Lambda Identifier
    ;

cameraParameterKey
    : 'viewpoint'
    | 'to'
    | 'lookAt'
    ;

ambienceDeclaration
    : 'ambience' floatList
    ;

opticsStatement
    : refractionDeclaration
    | transparencyDeclaration
    | reflectionDeclaration
    | diffusionDeclaration
    | luminousDeclaration
    | colourDeclaration
    ;

refractionDeclaration: 'refraction' Float;
transparencyDeclaration: 'transparency' Float;
reflectionDeclaration: 'reflection' Float;
diffusionDeclaration: 'diffusion' Float;
luminousDeclaration: 'luminous' BooleanValue;
colourDeclaration: 'colour' Float Float Float;

BooleanValue
    : TrueValue
    | FalseValue
    ;


floatList
    : Float (Float)*
    ;

Float
    : '-'? '0'..'9'+'.''0'..'9'+
    ;


Comment
    : '//' ~ [\r\n]*
    ;

BlockComment
    : '/*' .*? '*/' -> skip
    ;

Identifier : [a-z][a-zA-Z]+;

Separator: ',';
LineEnd : ';';
AssignOp: '=';

LeftBrace: '{';
RightBrace: '}';

Camera: '†';
Light: '☼';
Sphere: '◯';
Plane: '▭';
Vector: '→';
Diameter: 'Ø';
Brightness: 'Γ';
Lambda: 'λ';
Optics: 'optics';
OpticsDef: 'optics:';

TrueValue: 'yes';
FalseValue: 'no';

WS: [ \n\t\r]+ -> skip;
