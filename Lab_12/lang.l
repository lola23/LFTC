%{
	#include <stdio.h>
	#include <math.h> 
	int lines = 0;
%}
%option noyywrap
INTEGER		[1-9][0-9]*|0
IDENTIFIER	[a-zA-Z][a-zA-Z0-9]*
%%
"begin_appy" 	{printf( "Reserved word: %s\n", yytext );}
"end_appy"	{printf( "Reserved word: %s\n", yytext );}
"sparkle"		{printf( "Reserved word: %s\n", yytext );}
"inty"		{printf( "Reserved word: %s\n", yytext );}
"booly"		{printf( "Reserved word: %s\n", yytext );}
"ify"			{printf( "Reserved word: %s\n", yytext );}
"elsify"		{printf( "Reserved word: %s\n", yytext );}
"elsy"		{printf( "Reserved word: %s\n", yytext );}
"loopy"		{printf( "Reserved word: %s\n", yytext );}
to			{printf( "Reserved word: %s\n", yytext );}
"("		{printf( "Separator: %s\n", yytext );}
")"		{printf( "Separator: %s\n", yytext );}
"["		{printf( "Separator: %s\n", yytext );}
"]"		{printf( "Separator: %s\n", yytext );}
"{"		{printf( "Separator: %s\n", yytext );}
"}"		{printf( "Separator: %s\n", yytext );}
";"		{printf( "Separator: %s\n", yytext );}
","		{printf( "Separator: %s\n", yytext );}
"is"	{printf( "Operator: %s\n", yytext );}
">"		{printf( "Operator: %s\n", yytext );}
">="	{printf( "Operator: %s\n", yytext );}
"="		{printf( "Operator: %s\n", yytext );}
"!="	{printf( "Operator: %s\n", yytext );}
"<"		{printf( "Operator: %s\n", yytext );}
"<="	{printf( "Operator: %s\n", yytext );}
"+"		{printf( "Operator: %s\n", yytext );}
"-"		{printf( "Operator: %s\n", yytext );}
"*"		{printf( "Operator: %s\n", yytext );}
"/"		{printf( "Operator: %s\n", yytext );}
"%"		{printf( "Operator: %s\n", yytext );}
"and"	{printf( "Operator: %s\n", yytext );}
"or"	{printf( "Operator: %s\n", yytext );}
"not"	{printf( "Operator: %s\n", yytext );}
"true"			{printf( "Constant: %s\n", yytext );}
"false" 		{printf( "Constant: %s\n", yytext );}
{INTEGER}		{printf( "Constant: %s\n", yytext );}
{IDENTIFIER}	{printf( "Identifier: %s\n", yytext );}
[ \t]+ 	{}
[\n]+	{++lines;}
.		{printf("Illegal symbol at line\n"); return -1;}
%%
void main(argc, argv)
int argc;
char** argv; {           
if (argc > 1)
{
    FILE *file;
    file = fopen(argv[1], "r");
    if (!file)
    {
        fprintf(stderr, "Could not open %s\n", argv[1]);
        exit(1);
    }
    yyin = file;
}

yylex();
printf("Correct program!");
}
