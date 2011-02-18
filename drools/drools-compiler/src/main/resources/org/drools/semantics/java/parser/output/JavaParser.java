// $ANTLR 3.3 Nov 30, 2010 12:45:30 C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g 2011-02-13 03:04:18

	package org.drools.rule.builder.dialect.java.parser;
	import java.util.Iterator;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
/** A Java 1.5 grammar for ANTLR v3 derived from the spec
 *
 *  This is a very close representation of the spec; the changes
 *  are comestic (remove left recursion) and also fixes (the spec
 *  isn't exactly perfect).  I have run this on the 1.4.2 source
 *  and some nasty looking enums from 1.5, but have not really
 *  tested for 1.5 compatibility.
 *
 *  I built this with: java -Xmx100M org.antlr.Tool java.g 
 *  and got two errors that are ok (for now):
 *  java.g:691:9: Decision can match input such as
 *    "'0'..'9'{'E', 'e'}{'+', '-'}'0'..'9'{'D', 'F', 'd', 'f'}"
 *    using multiple alternatives: 3, 4
 *  As a result, alternative(s) 4 were disabled for that input
 *  java.g:734:35: Decision can match input such as "{'$', 'A'..'Z',
 *    '_', 'a'..'z', '\u00C0'..'\u00D6', '\u00D8'..'\u00F6',
 *    '\u00F8'..'\u1FFF', '\u3040'..'\u318F', '\u3300'..'\u337F',
 *    '\u3400'..'\u3D2D', '\u4E00'..'\u9FFF', '\uF900'..'\uFAFF'}"
 *    using multiple alternatives: 1, 2
 *  As a result, alternative(s) 2 were disabled for that input
 *
 *  You can turn enum on/off as a keyword :)
 *
 *  Version 1.0 -- initial release July 5, 2006 (requires 3.0b2 or higher)
 *
 *  Primary author: Terence Parr, July 2006
 *
 *  Version 1.0.1 -- corrections by Koen Vanderkimpen & Marko van Dooren,
 *      October 25, 2006;
 *      fixed normalInterfaceDeclaration: now uses typeParameters instead
 *          of typeParameter (according to JLS, 3rd edition)
 *      fixed castExpression: no longer allows expression next to type
 *          (according to semantics in JLS, in contrast with syntax in JLS)
 *
 *  Version 1.0.2 -- Terence Parr, Nov 27, 2006
 *      java spec I built this from had some bizarre for-loop control.
 *          Looked weird and so I looked elsewhere...Yep, it's messed up.
 *          simplified.
 *
 *  Version 1.0.3 -- Chris Hogue, Feb 26, 2007
 *      Factored out an annotationName rule and used it in the annotation rule.
 *          Not sure why, but typeName wasn't recognizing references to inner
 *          annotations (e.g. @InterfaceName.InnerAnnotation())
 *      Factored out the elementValue section of an annotation reference.  Created 
 *          elementValuePair and elementValuePairs rules, then used them in the 
 *          annotation rule.  Allows it to recognize annotation references with 
 *          multiple, comma separated attributes.
 *      Updated elementValueArrayInitializer so that it allows multiple elements.
 *          (It was only allowing 0 or 1 element).
 *      Updated localVariableDeclaration to allow annotations.  Interestingly the JLS
 *          doesn't appear to indicate this is legal, but it does work as of at least
 *          JDK 1.5.0_06.
 *      Moved the Identifier portion of annotationTypeElementRest to annotationMethodRest.
 *          Because annotationConstantRest already references variableDeclarator which 
 *          has the Identifier portion in it, the parser would fail on constants in 
 *          annotation definitions because it expected two identifiers.  
 *      Added optional trailing ';' to the alternatives in annotationTypeElementRest.
 *          Wouldn't handle an inner interface that has a trailing ';'.
 *      Swapped the expression and type rule reference order in castExpression to 
 *          make it check for genericized casts first.  It was failing to recognize a
 *          statement like  "Class<Byte> TYPE = (Class<Byte>)...;" because it was seeing
 *          'Class<Byte' in the cast expression as a less than expression, then failing 
 *          on the '>'.
 *      Changed createdName to use typeArguments instead of nonWildcardTypeArguments.
 *          Again, JLS doesn't seem to allow this, but java.lang.Class has an example of
 *          of this construct.
 *      Changed the 'this' alternative in primary to allow 'identifierSuffix' rather than
 *          just 'arguments'.  The case it couldn't handle was a call to an explicit
 *          generic method invocation (e.g. this.<E>doSomething()).  Using identifierSuffix
 *          may be overly aggressive--perhaps should create a more constrained thisSuffix rule?
 * 		
 *  Version 1.0.4 -- Hiroaki Nakamura, May 3, 2007
 *
 *	Fixed formalParameterDecls, localVariableDeclaration, forInit,
 *	and forVarControl to use variableModifier* not 'final'? (annotation)?
 */
public class JavaParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Identifier", "ENUM", "FloatingPointLiteral", "CharacterLiteral", "StringLiteral", "HexLiteral", "OctalLiteral", "DecimalLiteral", "HexDigit", "IntegerTypeSuffix", "Exponent", "FloatTypeSuffix", "EscapeSequence", "UnicodeEscape", "OctalEscape", "Letter", "JavaIDDigit", "WS", "COMMENT", "LINE_COMMENT", "'package'", "';'", "'import'", "'static'", "'.'", "'*'", "'class'", "'extends'", "'implements'", "'<'", "','", "'>'", "'&'", "'{'", "'}'", "'interface'", "'void'", "'['", "']'", "'throws'", "'='", "'public'", "'protected'", "'private'", "'abstract'", "'final'", "'native'", "'synchronized'", "'transient'", "'volatile'", "'strictfp'", "'boolean'", "'char'", "'byte'", "'short'", "'int'", "'long'", "'float'", "'double'", "'?'", "'super'", "'('", "')'", "'...'", "'null'", "'true'", "'false'", "'@'", "'default'", "'assert'", "':'", "'if'", "'else'", "'for'", "'while'", "'do'", "'try'", "'finally'", "'switch'", "'return'", "'throw'", "'break'", "'continue'", "'modify'", "'update'", "'retract'", "'exitPoints'", "'entryPoints'", "'channels'", "'catch'", "'case'", "'+='", "'-='", "'*='", "'/='", "'&='", "'|='", "'^='", "'%='", "'||'", "'&&'", "'|'", "'^'", "'=='", "'!='", "'instanceof'", "'+'", "'-'", "'/'", "'%'", "'++'", "'--'", "'~'", "'!'", "'this'", "'new'"
    };
    public static final int EOF=-1;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int T__29=29;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__50=50;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__59=59;
    public static final int T__60=60;
    public static final int T__61=61;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int T__73=73;
    public static final int T__74=74;
    public static final int T__75=75;
    public static final int T__76=76;
    public static final int T__77=77;
    public static final int T__78=78;
    public static final int T__79=79;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int T__84=84;
    public static final int T__85=85;
    public static final int T__86=86;
    public static final int T__87=87;
    public static final int T__88=88;
    public static final int T__89=89;
    public static final int T__90=90;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__93=93;
    public static final int T__94=94;
    public static final int T__95=95;
    public static final int T__96=96;
    public static final int T__97=97;
    public static final int T__98=98;
    public static final int T__99=99;
    public static final int T__100=100;
    public static final int T__101=101;
    public static final int T__102=102;
    public static final int T__103=103;
    public static final int T__104=104;
    public static final int T__105=105;
    public static final int T__106=106;
    public static final int T__107=107;
    public static final int T__108=108;
    public static final int T__109=109;
    public static final int T__110=110;
    public static final int T__111=111;
    public static final int T__112=112;
    public static final int T__113=113;
    public static final int T__114=114;
    public static final int T__115=115;
    public static final int T__116=116;
    public static final int T__117=117;
    public static final int T__118=118;
    public static final int T__119=119;
    public static final int Identifier=4;
    public static final int ENUM=5;
    public static final int FloatingPointLiteral=6;
    public static final int CharacterLiteral=7;
    public static final int StringLiteral=8;
    public static final int HexLiteral=9;
    public static final int OctalLiteral=10;
    public static final int DecimalLiteral=11;
    public static final int HexDigit=12;
    public static final int IntegerTypeSuffix=13;
    public static final int Exponent=14;
    public static final int FloatTypeSuffix=15;
    public static final int EscapeSequence=16;
    public static final int UnicodeEscape=17;
    public static final int OctalEscape=18;
    public static final int Letter=19;
    public static final int JavaIDDigit=20;
    public static final int WS=21;
    public static final int COMMENT=22;
    public static final int LINE_COMMENT=23;

    // delegates
    // delegators


        public JavaParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public JavaParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
            this.state.ruleMemo = new HashMap[415+1];
             
             
        }
        

    public String[] getTokenNames() { return JavaParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g"; }


    	private List identifiers = new ArrayList();
    	public List getIdentifiers() { return identifiers; }
    	private List localDeclarations = new ArrayList();
    	public List getLocalDeclarations() { return localDeclarations; }
    	public static final CommonToken IGNORE_TOKEN = new CommonToken(null,0,99,0,0);
    	private List errors = new ArrayList();
    	private int localVariableLevel = 0;
    	private List<JavaBlockDescr> blocks = new ArrayList<JavaBlockDescr>();
    	public List<JavaBlockDescr> getBlockDescr() { return blocks; }
    	
    	private String source = "unknown";
    	
    	public void setSource(String source) {
    		this.source = source;
    	}
    	
    	public String getSource() {
    		return this.source;
    	}
    		
    	public void reportError(RecognitionException ex) {
    	        // if we've already reported an error and have not matched a token
                    // yet successfully, don't report any errors.
                    if ( state.errorRecovery ) {
                            //System.err.print("[SPURIOUS] ");
                            return;
                    }
                    state.errorRecovery = true;

    		errors.add( ex );
    	}
         	
         	/** return the raw RecognitionException errors */
         	public List getErrors() {
         		return errors;
         	}
         	
         	/** Return a list of pretty strings summarising the errors */
         	public List getErrorMessages() {
         		List messages = new ArrayList();
     		for ( Iterator errorIter = errors.iterator() ; errorIter.hasNext() ; ) {
         	     		messages.add( createErrorMessage( (RecognitionException) errorIter.next() ) );
         	     	}
         	     	return messages;
         	}
         	
         	/** return true if any parser errors were accumulated */
         	public boolean hasErrors() {
      		return ! errors.isEmpty();
         	}
         	
         	/** This will take a RecognitionException, and create a sensible error message out of it */
         	public String createErrorMessage(RecognitionException e)
            {
    		StringBuilder message = new StringBuilder();
                    message.append( source + ":"+e.line+":"+e.charPositionInLine+" ");
                    if ( e instanceof MismatchedTokenException ) {
                            MismatchedTokenException mte = (MismatchedTokenException)e;
                            message.append("mismatched token: "+
                                                               e.token+
                                                               "; expecting type "+
                                                               tokenNames[mte.expecting]);
                    }
                    else if ( e instanceof MismatchedTreeNodeException ) {
                            MismatchedTreeNodeException mtne = (MismatchedTreeNodeException)e;
                            message.append("mismatched tree node: "+
                                                               mtne.node+
                                                               "; expecting type "+
                                                               tokenNames[mtne.expecting]);
                    }
                    else if ( e instanceof NoViableAltException ) {
                            NoViableAltException nvae = (NoViableAltException)e;
    			message.append( "Unexpected token '" + e.token.getText() + "'" );
                            /*
                            message.append("decision=<<"+nvae.grammarDecisionDescription+">>"+
                                                               " state "+nvae.stateNumber+
                                                               " (decision="+nvae.decisionNumber+
                                                               ") no viable alt; token="+
                                                               e.token);
                                                               */
                    }
                    else if ( e instanceof EarlyExitException ) {
                            EarlyExitException eee = (EarlyExitException)e;
                            message.append("required (...)+ loop (decision="+
                                                               eee.decisionNumber+
                                                               ") did not match anything; token="+
                                                               e.token);
                    }
                    else if ( e instanceof MismatchedSetException ) {
                            MismatchedSetException mse = (MismatchedSetException)e;
                            message.append("mismatched token '"+
                                                               e.token+
                                                               "' expecting set "+mse.expecting);
                    }
                    else if ( e instanceof MismatchedNotSetException ) {
                            MismatchedNotSetException mse = (MismatchedNotSetException)e;
                            message.append("mismatched token '"+
                                                               e.token+
                                                               "' expecting set "+mse.expecting);
                    }
                    else if ( e instanceof FailedPredicateException ) {
                            FailedPredicateException fpe = (FailedPredicateException)e;
                            message.append("rule "+fpe.ruleName+" failed predicate: {"+
                                                               fpe.predicateText+"}?");
    		}
                   	return message.toString();
            }



    // $ANTLR start "compilationUnit"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:206:1: compilationUnit : ( annotations )? ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* ;
    public final void compilationUnit() throws RecognitionException {
        int compilationUnit_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:207:2: ( ( annotations )? ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:207:4: ( annotations )? ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )*
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:207:4: ( annotations )?
            int alt1=2;
            alt1 = dfa1.predict(input);
            switch (alt1) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: annotations
                    {
                    pushFollow(FOLLOW_annotations_in_compilationUnit70);
                    annotations();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:208:3: ( packageDeclaration )?
            int alt2=2;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: packageDeclaration
                    {
                    pushFollow(FOLLOW_packageDeclaration_in_compilationUnit75);
                    packageDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:209:9: ( importDeclaration )*
            loop3:
            do {
                int alt3=2;
                alt3 = dfa3.predict(input);
                switch (alt3) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: importDeclaration
            	    {
            	    pushFollow(FOLLOW_importDeclaration_in_compilationUnit86);
            	    importDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:210:9: ( typeDeclaration )*
            loop4:
            do {
                int alt4=2;
                alt4 = dfa4.predict(input);
                switch (alt4) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: typeDeclaration
            	    {
            	    pushFollow(FOLLOW_typeDeclaration_in_compilationUnit97);
            	    typeDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 1, compilationUnit_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "compilationUnit"


    // $ANTLR start "packageDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:213:1: packageDeclaration : 'package' qualifiedName ';' ;
    public final void packageDeclaration() throws RecognitionException {
        int packageDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 2) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:214:2: ( 'package' qualifiedName ';' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:214:4: 'package' qualifiedName ';'
            {
            match(input,24,FOLLOW_24_in_packageDeclaration109); if (state.failed) return ;
            pushFollow(FOLLOW_qualifiedName_in_packageDeclaration111);
            qualifiedName();

            state._fsp--;
            if (state.failed) return ;
            match(input,25,FOLLOW_25_in_packageDeclaration113); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 2, packageDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "packageDeclaration"


    // $ANTLR start "importDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:217:1: importDeclaration : 'import' ( 'static' )? Identifier ( '.' Identifier )* ( '.' '*' )? ';' ;
    public final void importDeclaration() throws RecognitionException {
        int importDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:218:2: ( 'import' ( 'static' )? Identifier ( '.' Identifier )* ( '.' '*' )? ';' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:218:4: 'import' ( 'static' )? Identifier ( '.' Identifier )* ( '.' '*' )? ';'
            {
            match(input,26,FOLLOW_26_in_importDeclaration125); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:218:13: ( 'static' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==27) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: 'static'
                    {
                    match(input,27,FOLLOW_27_in_importDeclaration127); if (state.failed) return ;

                    }
                    break;

            }

            match(input,Identifier,FOLLOW_Identifier_in_importDeclaration130); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:218:34: ( '.' Identifier )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==28) ) {
                    int LA6_1 = input.LA(2);

                    if ( (LA6_1==Identifier) ) {
                        alt6=1;
                    }


                }


                switch (alt6) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:218:35: '.' Identifier
            	    {
            	    match(input,28,FOLLOW_28_in_importDeclaration133); if (state.failed) return ;
            	    match(input,Identifier,FOLLOW_Identifier_in_importDeclaration135); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:218:52: ( '.' '*' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==28) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:218:53: '.' '*'
                    {
                    match(input,28,FOLLOW_28_in_importDeclaration140); if (state.failed) return ;
                    match(input,29,FOLLOW_29_in_importDeclaration142); if (state.failed) return ;

                    }
                    break;

            }

            match(input,25,FOLLOW_25_in_importDeclaration146); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 3, importDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "importDeclaration"


    // $ANTLR start "typeDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:221:1: typeDeclaration : ( classOrInterfaceDeclaration | ';' );
    public final void typeDeclaration() throws RecognitionException {
        int typeDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:222:2: ( classOrInterfaceDeclaration | ';' )
            int alt8=2;
            alt8 = dfa8.predict(input);
            switch (alt8) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:222:4: classOrInterfaceDeclaration
                    {
                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration158);
                    classOrInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:223:9: ';'
                    {
                    match(input,25,FOLLOW_25_in_typeDeclaration168); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 4, typeDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "typeDeclaration"


    // $ANTLR start "classOrInterfaceDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:226:1: classOrInterfaceDeclaration : ( modifier )* ( classDeclaration | interfaceDeclaration ) ;
    public final void classOrInterfaceDeclaration() throws RecognitionException {
        int classOrInterfaceDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:227:2: ( ( modifier )* ( classDeclaration | interfaceDeclaration ) )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:227:4: ( modifier )* ( classDeclaration | interfaceDeclaration )
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:227:4: ( modifier )*
            loop9:
            do {
                int alt9=2;
                alt9 = dfa9.predict(input);
                switch (alt9) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: modifier
            	    {
            	    pushFollow(FOLLOW_modifier_in_classOrInterfaceDeclaration180);
            	    modifier();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:227:14: ( classDeclaration | interfaceDeclaration )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==ENUM||LA10_0==30) ) {
                alt10=1;
            }
            else if ( (LA10_0==39||LA10_0==71) ) {
                alt10=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:227:15: classDeclaration
                    {
                    pushFollow(FOLLOW_classDeclaration_in_classOrInterfaceDeclaration184);
                    classDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:227:34: interfaceDeclaration
                    {
                    pushFollow(FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration188);
                    interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 5, classOrInterfaceDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "classOrInterfaceDeclaration"


    // $ANTLR start "classDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:230:1: classDeclaration : ( normalClassDeclaration | enumDeclaration );
    public final void classDeclaration() throws RecognitionException {
        int classDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:231:2: ( normalClassDeclaration | enumDeclaration )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==30) ) {
                alt11=1;
            }
            else if ( (LA11_0==ENUM) ) {
                alt11=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:231:4: normalClassDeclaration
                    {
                    pushFollow(FOLLOW_normalClassDeclaration_in_classDeclaration201);
                    normalClassDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:232:9: enumDeclaration
                    {
                    pushFollow(FOLLOW_enumDeclaration_in_classDeclaration211);
                    enumDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 6, classDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "classDeclaration"


    // $ANTLR start "normalClassDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:235:1: normalClassDeclaration : 'class' Identifier ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody ;
    public final void normalClassDeclaration() throws RecognitionException {
        int normalClassDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:236:2: ( 'class' Identifier ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:236:4: 'class' Identifier ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody
            {
            match(input,30,FOLLOW_30_in_normalClassDeclaration223); if (state.failed) return ;
            match(input,Identifier,FOLLOW_Identifier_in_normalClassDeclaration225); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:236:23: ( typeParameters )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==33) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:236:24: typeParameters
                    {
                    pushFollow(FOLLOW_typeParameters_in_normalClassDeclaration228);
                    typeParameters();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:237:9: ( 'extends' type )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==31) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:237:10: 'extends' type
                    {
                    match(input,31,FOLLOW_31_in_normalClassDeclaration241); if (state.failed) return ;
                    pushFollow(FOLLOW_type_in_normalClassDeclaration243);
                    type();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:238:9: ( 'implements' typeList )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==32) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:238:10: 'implements' typeList
                    {
                    match(input,32,FOLLOW_32_in_normalClassDeclaration256); if (state.failed) return ;
                    pushFollow(FOLLOW_typeList_in_normalClassDeclaration258);
                    typeList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_classBody_in_normalClassDeclaration270);
            classBody();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 7, normalClassDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "normalClassDeclaration"


    // $ANTLR start "typeParameters"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:242:1: typeParameters : '<' typeParameter ( ',' typeParameter )* '>' ;
    public final void typeParameters() throws RecognitionException {
        int typeParameters_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:243:2: ( '<' typeParameter ( ',' typeParameter )* '>' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:243:4: '<' typeParameter ( ',' typeParameter )* '>'
            {
            match(input,33,FOLLOW_33_in_typeParameters282); if (state.failed) return ;
            pushFollow(FOLLOW_typeParameter_in_typeParameters284);
            typeParameter();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:243:22: ( ',' typeParameter )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==34) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:243:23: ',' typeParameter
            	    {
            	    match(input,34,FOLLOW_34_in_typeParameters287); if (state.failed) return ;
            	    pushFollow(FOLLOW_typeParameter_in_typeParameters289);
            	    typeParameter();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

            match(input,35,FOLLOW_35_in_typeParameters293); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 8, typeParameters_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "typeParameters"


    // $ANTLR start "typeParameter"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:246:1: typeParameter : Identifier ( 'extends' bound )? ;
    public final void typeParameter() throws RecognitionException {
        int typeParameter_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:247:2: ( Identifier ( 'extends' bound )? )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:247:4: Identifier ( 'extends' bound )?
            {
            match(input,Identifier,FOLLOW_Identifier_in_typeParameter304); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:247:15: ( 'extends' bound )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==31) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:247:16: 'extends' bound
                    {
                    match(input,31,FOLLOW_31_in_typeParameter307); if (state.failed) return ;
                    pushFollow(FOLLOW_bound_in_typeParameter309);
                    bound();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 9, typeParameter_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "typeParameter"


    // $ANTLR start "bound"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:250:1: bound : type ( '&' type )* ;
    public final void bound() throws RecognitionException {
        int bound_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 10) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:251:2: ( type ( '&' type )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:251:4: type ( '&' type )*
            {
            pushFollow(FOLLOW_type_in_bound324);
            type();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:251:9: ( '&' type )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==36) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:251:10: '&' type
            	    {
            	    match(input,36,FOLLOW_36_in_bound327); if (state.failed) return ;
            	    pushFollow(FOLLOW_type_in_bound329);
            	    type();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 10, bound_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "bound"


    // $ANTLR start "enumDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:254:1: enumDeclaration : ENUM Identifier ( 'implements' typeList )? enumBody ;
    public final void enumDeclaration() throws RecognitionException {
        int enumDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 11) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:255:2: ( ENUM Identifier ( 'implements' typeList )? enumBody )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:255:4: ENUM Identifier ( 'implements' typeList )? enumBody
            {
            match(input,ENUM,FOLLOW_ENUM_in_enumDeclaration342); if (state.failed) return ;
            match(input,Identifier,FOLLOW_Identifier_in_enumDeclaration344); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:255:20: ( 'implements' typeList )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==32) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:255:21: 'implements' typeList
                    {
                    match(input,32,FOLLOW_32_in_enumDeclaration347); if (state.failed) return ;
                    pushFollow(FOLLOW_typeList_in_enumDeclaration349);
                    typeList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_enumBody_in_enumDeclaration353);
            enumBody();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 11, enumDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "enumDeclaration"


    // $ANTLR start "enumBody"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:258:1: enumBody : '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' ;
    public final void enumBody() throws RecognitionException {
        int enumBody_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 12) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:259:2: ( '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:259:4: '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}'
            {
            match(input,37,FOLLOW_37_in_enumBody365); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:259:8: ( enumConstants )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==Identifier||LA19_0==71) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: enumConstants
                    {
                    pushFollow(FOLLOW_enumConstants_in_enumBody367);
                    enumConstants();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:259:23: ( ',' )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==34) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: ','
                    {
                    match(input,34,FOLLOW_34_in_enumBody370); if (state.failed) return ;

                    }
                    break;

            }

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:259:28: ( enumBodyDeclarations )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==25) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: enumBodyDeclarations
                    {
                    pushFollow(FOLLOW_enumBodyDeclarations_in_enumBody373);
                    enumBodyDeclarations();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            match(input,38,FOLLOW_38_in_enumBody376); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 12, enumBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "enumBody"


    // $ANTLR start "enumConstants"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:262:1: enumConstants : enumConstant ( ',' enumConstant )* ;
    public final void enumConstants() throws RecognitionException {
        int enumConstants_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 13) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:263:2: ( enumConstant ( ',' enumConstant )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:263:4: enumConstant ( ',' enumConstant )*
            {
            pushFollow(FOLLOW_enumConstant_in_enumConstants387);
            enumConstant();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:263:17: ( ',' enumConstant )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==34) ) {
                    int LA22_1 = input.LA(2);

                    if ( (LA22_1==Identifier||LA22_1==71) ) {
                        alt22=1;
                    }


                }


                switch (alt22) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:263:18: ',' enumConstant
            	    {
            	    match(input,34,FOLLOW_34_in_enumConstants390); if (state.failed) return ;
            	    pushFollow(FOLLOW_enumConstant_in_enumConstants392);
            	    enumConstant();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 13, enumConstants_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "enumConstants"


    // $ANTLR start "enumConstant"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:266:1: enumConstant : ( annotations )? Identifier ( arguments )? ( classBody )? ;
    public final void enumConstant() throws RecognitionException {
        int enumConstant_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 14) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:267:2: ( ( annotations )? Identifier ( arguments )? ( classBody )? )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:267:4: ( annotations )? Identifier ( arguments )? ( classBody )?
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:267:4: ( annotations )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==71) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: annotations
                    {
                    pushFollow(FOLLOW_annotations_in_enumConstant406);
                    annotations();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            match(input,Identifier,FOLLOW_Identifier_in_enumConstant409); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:267:28: ( arguments )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==65) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:267:29: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_enumConstant412);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:267:41: ( classBody )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==37) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:267:42: classBody
                    {
                    pushFollow(FOLLOW_classBody_in_enumConstant417);
                    classBody();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 14, enumConstant_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "enumConstant"


    // $ANTLR start "enumBodyDeclarations"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:270:1: enumBodyDeclarations : ';' ( classBodyDeclaration )* ;
    public final void enumBodyDeclarations() throws RecognitionException {
        int enumBodyDeclarations_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 15) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:271:2: ( ';' ( classBodyDeclaration )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:271:4: ';' ( classBodyDeclaration )*
            {
            match(input,25,FOLLOW_25_in_enumBodyDeclarations431); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:271:8: ( classBodyDeclaration )*
            loop26:
            do {
                int alt26=2;
                alt26 = dfa26.predict(input);
                switch (alt26) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:271:9: classBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_classBodyDeclaration_in_enumBodyDeclarations434);
            	    classBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 15, enumBodyDeclarations_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "enumBodyDeclarations"


    // $ANTLR start "interfaceDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:274:1: interfaceDeclaration : ( normalInterfaceDeclaration | annotationTypeDeclaration );
    public final void interfaceDeclaration() throws RecognitionException {
        int interfaceDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 16) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:275:2: ( normalInterfaceDeclaration | annotationTypeDeclaration )
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==39) ) {
                alt27=1;
            }
            else if ( (LA27_0==71) ) {
                alt27=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }
            switch (alt27) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:275:4: normalInterfaceDeclaration
                    {
                    pushFollow(FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration448);
                    normalInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:276:5: annotationTypeDeclaration
                    {
                    pushFollow(FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration454);
                    annotationTypeDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 16, interfaceDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceDeclaration"


    // $ANTLR start "normalInterfaceDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:279:1: normalInterfaceDeclaration : 'interface' Identifier ( typeParameters )? ( 'extends' typeList )? interfaceBody ;
    public final void normalInterfaceDeclaration() throws RecognitionException {
        int normalInterfaceDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 17) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:280:2: ( 'interface' Identifier ( typeParameters )? ( 'extends' typeList )? interfaceBody )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:280:4: 'interface' Identifier ( typeParameters )? ( 'extends' typeList )? interfaceBody
            {
            match(input,39,FOLLOW_39_in_normalInterfaceDeclaration466); if (state.failed) return ;
            match(input,Identifier,FOLLOW_Identifier_in_normalInterfaceDeclaration468); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:280:27: ( typeParameters )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==33) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: typeParameters
                    {
                    pushFollow(FOLLOW_typeParameters_in_normalInterfaceDeclaration470);
                    typeParameters();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:280:43: ( 'extends' typeList )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==31) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:280:44: 'extends' typeList
                    {
                    match(input,31,FOLLOW_31_in_normalInterfaceDeclaration474); if (state.failed) return ;
                    pushFollow(FOLLOW_typeList_in_normalInterfaceDeclaration476);
                    typeList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_interfaceBody_in_normalInterfaceDeclaration480);
            interfaceBody();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 17, normalInterfaceDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "normalInterfaceDeclaration"


    // $ANTLR start "typeList"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:283:1: typeList : type ( ',' type )* ;
    public final void typeList() throws RecognitionException {
        int typeList_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 18) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:284:2: ( type ( ',' type )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:284:4: type ( ',' type )*
            {
            pushFollow(FOLLOW_type_in_typeList492);
            type();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:284:9: ( ',' type )*
            loop30:
            do {
                int alt30=2;
                int LA30_0 = input.LA(1);

                if ( (LA30_0==34) ) {
                    alt30=1;
                }


                switch (alt30) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:284:10: ',' type
            	    {
            	    match(input,34,FOLLOW_34_in_typeList495); if (state.failed) return ;
            	    pushFollow(FOLLOW_type_in_typeList497);
            	    type();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop30;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 18, typeList_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "typeList"


    // $ANTLR start "classBody"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:287:1: classBody : '{' ( classBodyDeclaration )* '}' ;
    public final void classBody() throws RecognitionException {
        int classBody_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 19) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:288:2: ( '{' ( classBodyDeclaration )* '}' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:288:4: '{' ( classBodyDeclaration )* '}'
            {
            match(input,37,FOLLOW_37_in_classBody511); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:288:8: ( classBodyDeclaration )*
            loop31:
            do {
                int alt31=2;
                alt31 = dfa31.predict(input);
                switch (alt31) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: classBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_classBodyDeclaration_in_classBody513);
            	    classBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);

            match(input,38,FOLLOW_38_in_classBody516); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 19, classBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "classBody"


    // $ANTLR start "interfaceBody"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:291:1: interfaceBody : '{' ( interfaceBodyDeclaration )* '}' ;
    public final void interfaceBody() throws RecognitionException {
        int interfaceBody_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 20) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:292:2: ( '{' ( interfaceBodyDeclaration )* '}' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:292:4: '{' ( interfaceBodyDeclaration )* '}'
            {
            match(input,37,FOLLOW_37_in_interfaceBody528); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:292:8: ( interfaceBodyDeclaration )*
            loop32:
            do {
                int alt32=2;
                alt32 = dfa32.predict(input);
                switch (alt32) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: interfaceBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_interfaceBodyDeclaration_in_interfaceBody530);
            	    interfaceBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop32;
                }
            } while (true);

            match(input,38,FOLLOW_38_in_interfaceBody533); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 20, interfaceBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceBody"


    // $ANTLR start "classBodyDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:295:1: classBodyDeclaration : ( ';' | ( 'static' )? block | ( modifier )* memberDecl );
    public final void classBodyDeclaration() throws RecognitionException {
        int classBodyDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 21) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:296:2: ( ';' | ( 'static' )? block | ( modifier )* memberDecl )
            int alt35=3;
            alt35 = dfa35.predict(input);
            switch (alt35) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:296:4: ';'
                    {
                    match(input,25,FOLLOW_25_in_classBodyDeclaration544); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:297:4: ( 'static' )? block
                    {
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:297:4: ( 'static' )?
                    int alt33=2;
                    int LA33_0 = input.LA(1);

                    if ( (LA33_0==27) ) {
                        alt33=1;
                    }
                    switch (alt33) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: 'static'
                            {
                            match(input,27,FOLLOW_27_in_classBodyDeclaration549); if (state.failed) return ;

                            }
                            break;

                    }

                    pushFollow(FOLLOW_block_in_classBodyDeclaration552);
                    block();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:298:4: ( modifier )* memberDecl
                    {
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:298:4: ( modifier )*
                    loop34:
                    do {
                        int alt34=2;
                        alt34 = dfa34.predict(input);
                        switch (alt34) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: modifier
                    	    {
                    	    pushFollow(FOLLOW_modifier_in_classBodyDeclaration557);
                    	    modifier();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop34;
                        }
                    } while (true);

                    pushFollow(FOLLOW_memberDecl_in_classBodyDeclaration560);
                    memberDecl();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 21, classBodyDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "classBodyDeclaration"


    // $ANTLR start "memberDecl"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:301:1: memberDecl : ( genericMethodOrConstructorDecl | methodDeclaration | fieldDeclaration | 'void' Identifier voidMethodDeclaratorRest | Identifier constructorDeclaratorRest | interfaceDeclaration | classDeclaration );
    public final void memberDecl() throws RecognitionException {
        int memberDecl_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 22) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:302:2: ( genericMethodOrConstructorDecl | methodDeclaration | fieldDeclaration | 'void' Identifier voidMethodDeclaratorRest | Identifier constructorDeclaratorRest | interfaceDeclaration | classDeclaration )
            int alt36=7;
            alt36 = dfa36.predict(input);
            switch (alt36) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:302:4: genericMethodOrConstructorDecl
                    {
                    pushFollow(FOLLOW_genericMethodOrConstructorDecl_in_memberDecl572);
                    genericMethodOrConstructorDecl();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:303:4: methodDeclaration
                    {
                    pushFollow(FOLLOW_methodDeclaration_in_memberDecl577);
                    methodDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:304:4: fieldDeclaration
                    {
                    pushFollow(FOLLOW_fieldDeclaration_in_memberDecl582);
                    fieldDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:305:4: 'void' Identifier voidMethodDeclaratorRest
                    {
                    match(input,40,FOLLOW_40_in_memberDecl587); if (state.failed) return ;
                    match(input,Identifier,FOLLOW_Identifier_in_memberDecl589); if (state.failed) return ;
                    pushFollow(FOLLOW_voidMethodDeclaratorRest_in_memberDecl591);
                    voidMethodDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:306:4: Identifier constructorDeclaratorRest
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_memberDecl596); if (state.failed) return ;
                    pushFollow(FOLLOW_constructorDeclaratorRest_in_memberDecl598);
                    constructorDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:307:4: interfaceDeclaration
                    {
                    pushFollow(FOLLOW_interfaceDeclaration_in_memberDecl603);
                    interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:308:4: classDeclaration
                    {
                    pushFollow(FOLLOW_classDeclaration_in_memberDecl608);
                    classDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 22, memberDecl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "memberDecl"


    // $ANTLR start "genericMethodOrConstructorDecl"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:311:1: genericMethodOrConstructorDecl : typeParameters genericMethodOrConstructorRest ;
    public final void genericMethodOrConstructorDecl() throws RecognitionException {
        int genericMethodOrConstructorDecl_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 23) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:312:2: ( typeParameters genericMethodOrConstructorRest )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:312:4: typeParameters genericMethodOrConstructorRest
            {
            pushFollow(FOLLOW_typeParameters_in_genericMethodOrConstructorDecl620);
            typeParameters();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_genericMethodOrConstructorRest_in_genericMethodOrConstructorDecl622);
            genericMethodOrConstructorRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 23, genericMethodOrConstructorDecl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "genericMethodOrConstructorDecl"


    // $ANTLR start "genericMethodOrConstructorRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:315:1: genericMethodOrConstructorRest : ( ( type | 'void' ) Identifier methodDeclaratorRest | Identifier constructorDeclaratorRest );
    public final void genericMethodOrConstructorRest() throws RecognitionException {
        int genericMethodOrConstructorRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 24) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:316:2: ( ( type | 'void' ) Identifier methodDeclaratorRest | Identifier constructorDeclaratorRest )
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==Identifier) ) {
                int LA38_1 = input.LA(2);

                if ( (LA38_1==Identifier||LA38_1==28||LA38_1==33||LA38_1==41) ) {
                    alt38=1;
                }
                else if ( (LA38_1==65) ) {
                    alt38=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 38, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA38_0==40||(LA38_0>=55 && LA38_0<=62)) ) {
                alt38=1;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;
            }
            switch (alt38) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:316:4: ( type | 'void' ) Identifier methodDeclaratorRest
                    {
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:316:4: ( type | 'void' )
                    int alt37=2;
                    int LA37_0 = input.LA(1);

                    if ( (LA37_0==Identifier||(LA37_0>=55 && LA37_0<=62)) ) {
                        alt37=1;
                    }
                    else if ( (LA37_0==40) ) {
                        alt37=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 37, 0, input);

                        throw nvae;
                    }
                    switch (alt37) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:316:5: type
                            {
                            pushFollow(FOLLOW_type_in_genericMethodOrConstructorRest635);
                            type();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;
                        case 2 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:316:12: 'void'
                            {
                            match(input,40,FOLLOW_40_in_genericMethodOrConstructorRest639); if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,Identifier,FOLLOW_Identifier_in_genericMethodOrConstructorRest642); if (state.failed) return ;
                    pushFollow(FOLLOW_methodDeclaratorRest_in_genericMethodOrConstructorRest644);
                    methodDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:317:4: Identifier constructorDeclaratorRest
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_genericMethodOrConstructorRest649); if (state.failed) return ;
                    pushFollow(FOLLOW_constructorDeclaratorRest_in_genericMethodOrConstructorRest651);
                    constructorDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 24, genericMethodOrConstructorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "genericMethodOrConstructorRest"


    // $ANTLR start "methodDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:320:1: methodDeclaration : type Identifier methodDeclaratorRest ;
    public final void methodDeclaration() throws RecognitionException {
        int methodDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 25) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:321:2: ( type Identifier methodDeclaratorRest )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:321:4: type Identifier methodDeclaratorRest
            {
            pushFollow(FOLLOW_type_in_methodDeclaration662);
            type();

            state._fsp--;
            if (state.failed) return ;
            match(input,Identifier,FOLLOW_Identifier_in_methodDeclaration664); if (state.failed) return ;
            pushFollow(FOLLOW_methodDeclaratorRest_in_methodDeclaration666);
            methodDeclaratorRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 25, methodDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "methodDeclaration"


    // $ANTLR start "fieldDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:324:1: fieldDeclaration : type variableDeclarators ';' ;
    public final void fieldDeclaration() throws RecognitionException {
        int fieldDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 26) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:325:2: ( type variableDeclarators ';' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:325:4: type variableDeclarators ';'
            {
            pushFollow(FOLLOW_type_in_fieldDeclaration677);
            type();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_variableDeclarators_in_fieldDeclaration679);
            variableDeclarators();

            state._fsp--;
            if (state.failed) return ;
            match(input,25,FOLLOW_25_in_fieldDeclaration681); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 26, fieldDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "fieldDeclaration"


    // $ANTLR start "interfaceBodyDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:328:1: interfaceBodyDeclaration : ( ( modifier )* interfaceMemberDecl | ';' );
    public final void interfaceBodyDeclaration() throws RecognitionException {
        int interfaceBodyDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 27) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:329:2: ( ( modifier )* interfaceMemberDecl | ';' )
            int alt40=2;
            alt40 = dfa40.predict(input);
            switch (alt40) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:329:4: ( modifier )* interfaceMemberDecl
                    {
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:329:4: ( modifier )*
                    loop39:
                    do {
                        int alt39=2;
                        alt39 = dfa39.predict(input);
                        switch (alt39) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: modifier
                    	    {
                    	    pushFollow(FOLLOW_modifier_in_interfaceBodyDeclaration694);
                    	    modifier();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop39;
                        }
                    } while (true);

                    pushFollow(FOLLOW_interfaceMemberDecl_in_interfaceBodyDeclaration697);
                    interfaceMemberDecl();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:330:6: ';'
                    {
                    match(input,25,FOLLOW_25_in_interfaceBodyDeclaration704); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 27, interfaceBodyDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceBodyDeclaration"


    // $ANTLR start "interfaceMemberDecl"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:333:1: interfaceMemberDecl : ( interfaceMethodOrFieldDecl | interfaceGenericMethodDecl | 'void' Identifier voidInterfaceMethodDeclaratorRest | interfaceDeclaration | classDeclaration );
    public final void interfaceMemberDecl() throws RecognitionException {
        int interfaceMemberDecl_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 28) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:334:2: ( interfaceMethodOrFieldDecl | interfaceGenericMethodDecl | 'void' Identifier voidInterfaceMethodDeclaratorRest | interfaceDeclaration | classDeclaration )
            int alt41=5;
            switch ( input.LA(1) ) {
            case Identifier:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
                {
                alt41=1;
                }
                break;
            case 33:
                {
                alt41=2;
                }
                break;
            case 40:
                {
                alt41=3;
                }
                break;
            case 39:
            case 71:
                {
                alt41=4;
                }
                break;
            case ENUM:
            case 30:
                {
                alt41=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 41, 0, input);

                throw nvae;
            }

            switch (alt41) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:334:4: interfaceMethodOrFieldDecl
                    {
                    pushFollow(FOLLOW_interfaceMethodOrFieldDecl_in_interfaceMemberDecl715);
                    interfaceMethodOrFieldDecl();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:335:6: interfaceGenericMethodDecl
                    {
                    pushFollow(FOLLOW_interfaceGenericMethodDecl_in_interfaceMemberDecl722);
                    interfaceGenericMethodDecl();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:336:9: 'void' Identifier voidInterfaceMethodDeclaratorRest
                    {
                    match(input,40,FOLLOW_40_in_interfaceMemberDecl732); if (state.failed) return ;
                    match(input,Identifier,FOLLOW_Identifier_in_interfaceMemberDecl734); if (state.failed) return ;
                    pushFollow(FOLLOW_voidInterfaceMethodDeclaratorRest_in_interfaceMemberDecl736);
                    voidInterfaceMethodDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:337:9: interfaceDeclaration
                    {
                    pushFollow(FOLLOW_interfaceDeclaration_in_interfaceMemberDecl746);
                    interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:338:9: classDeclaration
                    {
                    pushFollow(FOLLOW_classDeclaration_in_interfaceMemberDecl756);
                    classDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 28, interfaceMemberDecl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceMemberDecl"


    // $ANTLR start "interfaceMethodOrFieldDecl"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:341:1: interfaceMethodOrFieldDecl : type Identifier interfaceMethodOrFieldRest ;
    public final void interfaceMethodOrFieldDecl() throws RecognitionException {
        int interfaceMethodOrFieldDecl_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 29) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:342:2: ( type Identifier interfaceMethodOrFieldRest )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:342:4: type Identifier interfaceMethodOrFieldRest
            {
            pushFollow(FOLLOW_type_in_interfaceMethodOrFieldDecl768);
            type();

            state._fsp--;
            if (state.failed) return ;
            match(input,Identifier,FOLLOW_Identifier_in_interfaceMethodOrFieldDecl770); if (state.failed) return ;
            pushFollow(FOLLOW_interfaceMethodOrFieldRest_in_interfaceMethodOrFieldDecl772);
            interfaceMethodOrFieldRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 29, interfaceMethodOrFieldDecl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceMethodOrFieldDecl"


    // $ANTLR start "interfaceMethodOrFieldRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:345:1: interfaceMethodOrFieldRest : ( constantDeclaratorsRest ';' | interfaceMethodDeclaratorRest );
    public final void interfaceMethodOrFieldRest() throws RecognitionException {
        int interfaceMethodOrFieldRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 30) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:346:2: ( constantDeclaratorsRest ';' | interfaceMethodDeclaratorRest )
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==41||LA42_0==44) ) {
                alt42=1;
            }
            else if ( (LA42_0==65) ) {
                alt42=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 42, 0, input);

                throw nvae;
            }
            switch (alt42) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:346:4: constantDeclaratorsRest ';'
                    {
                    pushFollow(FOLLOW_constantDeclaratorsRest_in_interfaceMethodOrFieldRest784);
                    constantDeclaratorsRest();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,25,FOLLOW_25_in_interfaceMethodOrFieldRest786); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:347:4: interfaceMethodDeclaratorRest
                    {
                    pushFollow(FOLLOW_interfaceMethodDeclaratorRest_in_interfaceMethodOrFieldRest791);
                    interfaceMethodDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 30, interfaceMethodOrFieldRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceMethodOrFieldRest"


    // $ANTLR start "methodDeclaratorRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:350:1: methodDeclaratorRest : formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( methodBody | ';' ) ;
    public final void methodDeclaratorRest() throws RecognitionException {
        int methodDeclaratorRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 31) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:351:2: ( formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( methodBody | ';' ) )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:351:4: formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( methodBody | ';' )
            {
            pushFollow(FOLLOW_formalParameters_in_methodDeclaratorRest803);
            formalParameters();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:351:21: ( '[' ']' )*
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( (LA43_0==41) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:351:22: '[' ']'
            	    {
            	    match(input,41,FOLLOW_41_in_methodDeclaratorRest806); if (state.failed) return ;
            	    match(input,42,FOLLOW_42_in_methodDeclaratorRest808); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop43;
                }
            } while (true);

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:352:9: ( 'throws' qualifiedNameList )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==43) ) {
                alt44=1;
            }
            switch (alt44) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:352:10: 'throws' qualifiedNameList
                    {
                    match(input,43,FOLLOW_43_in_methodDeclaratorRest821); if (state.failed) return ;
                    pushFollow(FOLLOW_qualifiedNameList_in_methodDeclaratorRest823);
                    qualifiedNameList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:353:9: ( methodBody | ';' )
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==37) ) {
                alt45=1;
            }
            else if ( (LA45_0==25) ) {
                alt45=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 45, 0, input);

                throw nvae;
            }
            switch (alt45) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:353:13: methodBody
                    {
                    pushFollow(FOLLOW_methodBody_in_methodDeclaratorRest839);
                    methodBody();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:354:13: ';'
                    {
                    match(input,25,FOLLOW_25_in_methodDeclaratorRest853); if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 31, methodDeclaratorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "methodDeclaratorRest"


    // $ANTLR start "voidMethodDeclaratorRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:358:1: voidMethodDeclaratorRest : formalParameters ( 'throws' qualifiedNameList )? ( methodBody | ';' ) ;
    public final void voidMethodDeclaratorRest() throws RecognitionException {
        int voidMethodDeclaratorRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 32) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:359:2: ( formalParameters ( 'throws' qualifiedNameList )? ( methodBody | ';' ) )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:359:4: formalParameters ( 'throws' qualifiedNameList )? ( methodBody | ';' )
            {
            pushFollow(FOLLOW_formalParameters_in_voidMethodDeclaratorRest875);
            formalParameters();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:359:21: ( 'throws' qualifiedNameList )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==43) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:359:22: 'throws' qualifiedNameList
                    {
                    match(input,43,FOLLOW_43_in_voidMethodDeclaratorRest878); if (state.failed) return ;
                    pushFollow(FOLLOW_qualifiedNameList_in_voidMethodDeclaratorRest880);
                    qualifiedNameList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:360:9: ( methodBody | ';' )
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( (LA47_0==37) ) {
                alt47=1;
            }
            else if ( (LA47_0==25) ) {
                alt47=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 47, 0, input);

                throw nvae;
            }
            switch (alt47) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:360:13: methodBody
                    {
                    pushFollow(FOLLOW_methodBody_in_voidMethodDeclaratorRest896);
                    methodBody();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:361:13: ';'
                    {
                    match(input,25,FOLLOW_25_in_voidMethodDeclaratorRest910); if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 32, voidMethodDeclaratorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "voidMethodDeclaratorRest"


    // $ANTLR start "interfaceMethodDeclaratorRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:365:1: interfaceMethodDeclaratorRest : formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';' ;
    public final void interfaceMethodDeclaratorRest() throws RecognitionException {
        int interfaceMethodDeclaratorRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 33) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:366:2: ( formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:366:4: formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';'
            {
            pushFollow(FOLLOW_formalParameters_in_interfaceMethodDeclaratorRest932);
            formalParameters();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:366:21: ( '[' ']' )*
            loop48:
            do {
                int alt48=2;
                int LA48_0 = input.LA(1);

                if ( (LA48_0==41) ) {
                    alt48=1;
                }


                switch (alt48) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:366:22: '[' ']'
            	    {
            	    match(input,41,FOLLOW_41_in_interfaceMethodDeclaratorRest935); if (state.failed) return ;
            	    match(input,42,FOLLOW_42_in_interfaceMethodDeclaratorRest937); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop48;
                }
            } while (true);

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:366:32: ( 'throws' qualifiedNameList )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==43) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:366:33: 'throws' qualifiedNameList
                    {
                    match(input,43,FOLLOW_43_in_interfaceMethodDeclaratorRest942); if (state.failed) return ;
                    pushFollow(FOLLOW_qualifiedNameList_in_interfaceMethodDeclaratorRest944);
                    qualifiedNameList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            match(input,25,FOLLOW_25_in_interfaceMethodDeclaratorRest948); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 33, interfaceMethodDeclaratorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceMethodDeclaratorRest"


    // $ANTLR start "interfaceGenericMethodDecl"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:369:1: interfaceGenericMethodDecl : typeParameters ( type | 'void' ) Identifier interfaceMethodDeclaratorRest ;
    public final void interfaceGenericMethodDecl() throws RecognitionException {
        int interfaceGenericMethodDecl_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 34) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:370:2: ( typeParameters ( type | 'void' ) Identifier interfaceMethodDeclaratorRest )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:370:4: typeParameters ( type | 'void' ) Identifier interfaceMethodDeclaratorRest
            {
            pushFollow(FOLLOW_typeParameters_in_interfaceGenericMethodDecl960);
            typeParameters();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:370:19: ( type | 'void' )
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==Identifier||(LA50_0>=55 && LA50_0<=62)) ) {
                alt50=1;
            }
            else if ( (LA50_0==40) ) {
                alt50=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;
            }
            switch (alt50) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:370:20: type
                    {
                    pushFollow(FOLLOW_type_in_interfaceGenericMethodDecl963);
                    type();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:370:27: 'void'
                    {
                    match(input,40,FOLLOW_40_in_interfaceGenericMethodDecl967); if (state.failed) return ;

                    }
                    break;

            }

            match(input,Identifier,FOLLOW_Identifier_in_interfaceGenericMethodDecl970); if (state.failed) return ;
            pushFollow(FOLLOW_interfaceMethodDeclaratorRest_in_interfaceGenericMethodDecl980);
            interfaceMethodDeclaratorRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 34, interfaceGenericMethodDecl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceGenericMethodDecl"


    // $ANTLR start "voidInterfaceMethodDeclaratorRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:374:1: voidInterfaceMethodDeclaratorRest : formalParameters ( 'throws' qualifiedNameList )? ';' ;
    public final void voidInterfaceMethodDeclaratorRest() throws RecognitionException {
        int voidInterfaceMethodDeclaratorRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 35) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:375:2: ( formalParameters ( 'throws' qualifiedNameList )? ';' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:375:4: formalParameters ( 'throws' qualifiedNameList )? ';'
            {
            pushFollow(FOLLOW_formalParameters_in_voidInterfaceMethodDeclaratorRest992);
            formalParameters();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:375:21: ( 'throws' qualifiedNameList )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==43) ) {
                alt51=1;
            }
            switch (alt51) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:375:22: 'throws' qualifiedNameList
                    {
                    match(input,43,FOLLOW_43_in_voidInterfaceMethodDeclaratorRest995); if (state.failed) return ;
                    pushFollow(FOLLOW_qualifiedNameList_in_voidInterfaceMethodDeclaratorRest997);
                    qualifiedNameList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            match(input,25,FOLLOW_25_in_voidInterfaceMethodDeclaratorRest1001); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 35, voidInterfaceMethodDeclaratorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "voidInterfaceMethodDeclaratorRest"


    // $ANTLR start "constructorDeclaratorRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:378:1: constructorDeclaratorRest : formalParameters ( 'throws' qualifiedNameList )? methodBody ;
    public final void constructorDeclaratorRest() throws RecognitionException {
        int constructorDeclaratorRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 36) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:379:2: ( formalParameters ( 'throws' qualifiedNameList )? methodBody )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:379:4: formalParameters ( 'throws' qualifiedNameList )? methodBody
            {
            pushFollow(FOLLOW_formalParameters_in_constructorDeclaratorRest1013);
            formalParameters();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:379:21: ( 'throws' qualifiedNameList )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( (LA52_0==43) ) {
                alt52=1;
            }
            switch (alt52) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:379:22: 'throws' qualifiedNameList
                    {
                    match(input,43,FOLLOW_43_in_constructorDeclaratorRest1016); if (state.failed) return ;
                    pushFollow(FOLLOW_qualifiedNameList_in_constructorDeclaratorRest1018);
                    qualifiedNameList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_methodBody_in_constructorDeclaratorRest1022);
            methodBody();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 36, constructorDeclaratorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "constructorDeclaratorRest"


    // $ANTLR start "constantDeclarator"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:382:1: constantDeclarator : Identifier constantDeclaratorRest ;
    public final void constantDeclarator() throws RecognitionException {
        int constantDeclarator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 37) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:383:2: ( Identifier constantDeclaratorRest )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:383:4: Identifier constantDeclaratorRest
            {
            match(input,Identifier,FOLLOW_Identifier_in_constantDeclarator1033); if (state.failed) return ;
            pushFollow(FOLLOW_constantDeclaratorRest_in_constantDeclarator1035);
            constantDeclaratorRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 37, constantDeclarator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "constantDeclarator"


    // $ANTLR start "variableDeclarators"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:386:1: variableDeclarators : variableDeclarator ( ',' variableDeclarator )* ;
    public final void variableDeclarators() throws RecognitionException {
        int variableDeclarators_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 38) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:387:2: ( variableDeclarator ( ',' variableDeclarator )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:387:4: variableDeclarator ( ',' variableDeclarator )*
            {
            pushFollow(FOLLOW_variableDeclarator_in_variableDeclarators1047);
            variableDeclarator();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:387:23: ( ',' variableDeclarator )*
            loop53:
            do {
                int alt53=2;
                int LA53_0 = input.LA(1);

                if ( (LA53_0==34) ) {
                    alt53=1;
                }


                switch (alt53) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:387:24: ',' variableDeclarator
            	    {
            	    match(input,34,FOLLOW_34_in_variableDeclarators1050); if (state.failed) return ;
            	    pushFollow(FOLLOW_variableDeclarator_in_variableDeclarators1052);
            	    variableDeclarator();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop53;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 38, variableDeclarators_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "variableDeclarators"

    protected static class variableDeclarator_scope {
        JavaLocalDeclarationDescr.IdentifierDescr ident;
    }
    protected Stack variableDeclarator_stack = new Stack();


    // $ANTLR start "variableDeclarator"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:390:1: variableDeclarator : id= Identifier rest= variableDeclaratorRest ;
    public final void variableDeclarator() throws RecognitionException {
        variableDeclarator_stack.push(new variableDeclarator_scope());
        int variableDeclarator_StartIndex = input.index();
        Token id=null;
        JavaParser.variableDeclaratorRest_return rest = null;



        		if( this.localVariableLevel == 1 ) { // we only want top level local vars
        			((variableDeclarator_scope)variableDeclarator_stack.peek()).ident = new JavaLocalDeclarationDescr.IdentifierDescr();
        		}
        	
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 39) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:404:2: (id= Identifier rest= variableDeclaratorRest )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:404:4: id= Identifier rest= variableDeclaratorRest
            {
            id=(Token)match(input,Identifier,FOLLOW_Identifier_in_variableDeclarator1084); if (state.failed) return ;
            pushFollow(FOLLOW_variableDeclaratorRest_in_variableDeclarator1088);
            rest=variableDeclaratorRest();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               
              			if( this.localVariableLevel == 1 ) { // we only want top level local vars
              				((variableDeclarator_scope)variableDeclarator_stack.peek()).ident.setIdentifier( (id!=null?id.getText():null) );
              				((variableDeclarator_scope)variableDeclarator_stack.peek()).ident.setStart( ((CommonToken)id).getStartIndex() - 1 );
              				if( (rest!=null?((Token)rest.stop):null) != null ) {
                 					((variableDeclarator_scope)variableDeclarator_stack.peek()).ident.setEnd( ((CommonToken)(rest!=null?((Token)rest.stop):null)).getStopIndex() );
              				}
              			}
              		
            }

            }

            if ( state.backtracking==0 ) {

              	        if( this.localVariableLevel == 1 ) { // we only want top level local vars
              	        	((localVariableDeclaration_scope)localVariableDeclaration_stack.peek()).descr.addIdentifier( ((variableDeclarator_scope)variableDeclarator_stack.peek()).ident );
              	        }
              	
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 39, variableDeclarator_StartIndex); }
            variableDeclarator_stack.pop();
        }
        return ;
    }
    // $ANTLR end "variableDeclarator"

    public static class variableDeclaratorRest_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "variableDeclaratorRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:416:1: variableDeclaratorRest : ( ( '[' ']' )+ ( '=' variableInitializer )? | '=' variableInitializer | );
    public final JavaParser.variableDeclaratorRest_return variableDeclaratorRest() throws RecognitionException {
        JavaParser.variableDeclaratorRest_return retval = new JavaParser.variableDeclaratorRest_return();
        retval.start = input.LT(1);
        int variableDeclaratorRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 40) ) { return retval; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:417:2: ( ( '[' ']' )+ ( '=' variableInitializer )? | '=' variableInitializer | )
            int alt56=3;
            switch ( input.LA(1) ) {
            case 41:
                {
                alt56=1;
                }
                break;
            case 44:
                {
                alt56=2;
                }
                break;
            case EOF:
            case 25:
            case 34:
                {
                alt56=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }

            switch (alt56) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:417:4: ( '[' ']' )+ ( '=' variableInitializer )?
                    {
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:417:4: ( '[' ']' )+
                    int cnt54=0;
                    loop54:
                    do {
                        int alt54=2;
                        int LA54_0 = input.LA(1);

                        if ( (LA54_0==41) ) {
                            alt54=1;
                        }


                        switch (alt54) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:417:5: '[' ']'
                    	    {
                    	    match(input,41,FOLLOW_41_in_variableDeclaratorRest1106); if (state.failed) return retval;
                    	    match(input,42,FOLLOW_42_in_variableDeclaratorRest1108); if (state.failed) return retval;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt54 >= 1 ) break loop54;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(54, input);
                                throw eee;
                        }
                        cnt54++;
                    } while (true);

                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:417:15: ( '=' variableInitializer )?
                    int alt55=2;
                    int LA55_0 = input.LA(1);

                    if ( (LA55_0==44) ) {
                        alt55=1;
                    }
                    switch (alt55) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:417:16: '=' variableInitializer
                            {
                            match(input,44,FOLLOW_44_in_variableDeclaratorRest1113); if (state.failed) return retval;
                            pushFollow(FOLLOW_variableInitializer_in_variableDeclaratorRest1115);
                            variableInitializer();

                            state._fsp--;
                            if (state.failed) return retval;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:418:4: '=' variableInitializer
                    {
                    match(input,44,FOLLOW_44_in_variableDeclaratorRest1122); if (state.failed) return retval;
                    pushFollow(FOLLOW_variableInitializer_in_variableDeclaratorRest1124);
                    variableInitializer();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:420:2: 
                    {
                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 40, variableDeclaratorRest_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "variableDeclaratorRest"


    // $ANTLR start "constantDeclaratorsRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:422:1: constantDeclaratorsRest : constantDeclaratorRest ( ',' constantDeclarator )* ;
    public final void constantDeclaratorsRest() throws RecognitionException {
        int constantDeclaratorsRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 41) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:423:5: ( constantDeclaratorRest ( ',' constantDeclarator )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:423:9: constantDeclaratorRest ( ',' constantDeclarator )*
            {
            pushFollow(FOLLOW_constantDeclaratorRest_in_constantDeclaratorsRest1144);
            constantDeclaratorRest();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:423:32: ( ',' constantDeclarator )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==34) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:423:33: ',' constantDeclarator
            	    {
            	    match(input,34,FOLLOW_34_in_constantDeclaratorsRest1147); if (state.failed) return ;
            	    pushFollow(FOLLOW_constantDeclarator_in_constantDeclaratorsRest1149);
            	    constantDeclarator();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop57;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 41, constantDeclaratorsRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "constantDeclaratorsRest"


    // $ANTLR start "constantDeclaratorRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:426:1: constantDeclaratorRest : ( '[' ']' )* '=' variableInitializer ;
    public final void constantDeclaratorRest() throws RecognitionException {
        int constantDeclaratorRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 42) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:427:2: ( ( '[' ']' )* '=' variableInitializer )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:427:4: ( '[' ']' )* '=' variableInitializer
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:427:4: ( '[' ']' )*
            loop58:
            do {
                int alt58=2;
                int LA58_0 = input.LA(1);

                if ( (LA58_0==41) ) {
                    alt58=1;
                }


                switch (alt58) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:427:5: '[' ']'
            	    {
            	    match(input,41,FOLLOW_41_in_constantDeclaratorRest1166); if (state.failed) return ;
            	    match(input,42,FOLLOW_42_in_constantDeclaratorRest1168); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop58;
                }
            } while (true);

            match(input,44,FOLLOW_44_in_constantDeclaratorRest1172); if (state.failed) return ;
            pushFollow(FOLLOW_variableInitializer_in_constantDeclaratorRest1174);
            variableInitializer();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 42, constantDeclaratorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "constantDeclaratorRest"


    // $ANTLR start "variableDeclaratorId"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:430:1: variableDeclaratorId : Identifier ( '[' ']' )* ;
    public final void variableDeclaratorId() throws RecognitionException {
        int variableDeclaratorId_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 43) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:431:2: ( Identifier ( '[' ']' )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:431:4: Identifier ( '[' ']' )*
            {
            match(input,Identifier,FOLLOW_Identifier_in_variableDeclaratorId1186); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:431:15: ( '[' ']' )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==41) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:431:16: '[' ']'
            	    {
            	    match(input,41,FOLLOW_41_in_variableDeclaratorId1189); if (state.failed) return ;
            	    match(input,42,FOLLOW_42_in_variableDeclaratorId1191); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 43, variableDeclaratorId_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "variableDeclaratorId"


    // $ANTLR start "variableInitializer"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:434:1: variableInitializer : ( arrayInitializer | expression );
    public final void variableInitializer() throws RecognitionException {
        int variableInitializer_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 44) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:435:2: ( arrayInitializer | expression )
            int alt60=2;
            alt60 = dfa60.predict(input);
            switch (alt60) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:435:4: arrayInitializer
                    {
                    pushFollow(FOLLOW_arrayInitializer_in_variableInitializer1204);
                    arrayInitializer();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:436:9: expression
                    {
                    pushFollow(FOLLOW_expression_in_variableInitializer1214);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 44, variableInitializer_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "variableInitializer"


    // $ANTLR start "arrayInitializer"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:439:1: arrayInitializer : '{' ( variableInitializer ( ',' variableInitializer )* ( ',' )? )? '}' ;
    public final void arrayInitializer() throws RecognitionException {
        int arrayInitializer_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 45) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:440:2: ( '{' ( variableInitializer ( ',' variableInitializer )* ( ',' )? )? '}' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:440:4: '{' ( variableInitializer ( ',' variableInitializer )* ( ',' )? )? '}'
            {
            match(input,37,FOLLOW_37_in_arrayInitializer1226); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:440:8: ( variableInitializer ( ',' variableInitializer )* ( ',' )? )?
            int alt63=2;
            alt63 = dfa63.predict(input);
            switch (alt63) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:440:9: variableInitializer ( ',' variableInitializer )* ( ',' )?
                    {
                    pushFollow(FOLLOW_variableInitializer_in_arrayInitializer1229);
                    variableInitializer();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:440:29: ( ',' variableInitializer )*
                    loop61:
                    do {
                        int alt61=2;
                        alt61 = dfa61.predict(input);
                        switch (alt61) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:440:30: ',' variableInitializer
                    	    {
                    	    match(input,34,FOLLOW_34_in_arrayInitializer1232); if (state.failed) return ;
                    	    pushFollow(FOLLOW_variableInitializer_in_arrayInitializer1234);
                    	    variableInitializer();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop61;
                        }
                    } while (true);

                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:440:56: ( ',' )?
                    int alt62=2;
                    int LA62_0 = input.LA(1);

                    if ( (LA62_0==34) ) {
                        alt62=1;
                    }
                    switch (alt62) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:440:57: ','
                            {
                            match(input,34,FOLLOW_34_in_arrayInitializer1239); if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }

            match(input,38,FOLLOW_38_in_arrayInitializer1246); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 45, arrayInitializer_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "arrayInitializer"


    // $ANTLR start "modifier"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:443:1: modifier : ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' );
    public final void modifier() throws RecognitionException {
        int modifier_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 46) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:444:5: ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )
            int alt64=12;
            alt64 = dfa64.predict(input);
            switch (alt64) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:444:9: annotation
                    {
                    pushFollow(FOLLOW_annotation_in_modifier1262);
                    annotation();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:445:9: 'public'
                    {
                    match(input,45,FOLLOW_45_in_modifier1272); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:446:9: 'protected'
                    {
                    match(input,46,FOLLOW_46_in_modifier1282); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:447:9: 'private'
                    {
                    match(input,47,FOLLOW_47_in_modifier1292); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:448:9: 'static'
                    {
                    match(input,27,FOLLOW_27_in_modifier1302); if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:449:9: 'abstract'
                    {
                    match(input,48,FOLLOW_48_in_modifier1312); if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:450:9: 'final'
                    {
                    match(input,49,FOLLOW_49_in_modifier1322); if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:451:9: 'native'
                    {
                    match(input,50,FOLLOW_50_in_modifier1332); if (state.failed) return ;

                    }
                    break;
                case 9 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:452:9: 'synchronized'
                    {
                    match(input,51,FOLLOW_51_in_modifier1342); if (state.failed) return ;

                    }
                    break;
                case 10 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:453:9: 'transient'
                    {
                    match(input,52,FOLLOW_52_in_modifier1352); if (state.failed) return ;

                    }
                    break;
                case 11 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:454:9: 'volatile'
                    {
                    match(input,53,FOLLOW_53_in_modifier1362); if (state.failed) return ;

                    }
                    break;
                case 12 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:455:9: 'strictfp'
                    {
                    match(input,54,FOLLOW_54_in_modifier1372); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 46, modifier_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "modifier"


    // $ANTLR start "packageOrTypeName"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:458:1: packageOrTypeName : Identifier ( '.' Identifier )* ;
    public final void packageOrTypeName() throws RecognitionException {
        int packageOrTypeName_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 47) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:459:2: ( Identifier ( '.' Identifier )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:459:4: Identifier ( '.' Identifier )*
            {
            match(input,Identifier,FOLLOW_Identifier_in_packageOrTypeName1386); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:459:15: ( '.' Identifier )*
            loop65:
            do {
                int alt65=2;
                int LA65_0 = input.LA(1);

                if ( (LA65_0==28) ) {
                    int LA65_1 = input.LA(2);

                    if ( (LA65_1==Identifier) ) {
                        int LA65_2 = input.LA(3);

                        if ( (synpred85_Java()) ) {
                            alt65=1;
                        }


                    }


                }


                switch (alt65) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:459:16: '.' Identifier
            	    {
            	    match(input,28,FOLLOW_28_in_packageOrTypeName1389); if (state.failed) return ;
            	    match(input,Identifier,FOLLOW_Identifier_in_packageOrTypeName1391); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop65;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 47, packageOrTypeName_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "packageOrTypeName"


    // $ANTLR start "enumConstantName"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:462:1: enumConstantName : Identifier ;
    public final void enumConstantName() throws RecognitionException {
        int enumConstantName_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 48) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:463:5: ( Identifier )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:463:9: Identifier
            {
            match(input,Identifier,FOLLOW_Identifier_in_enumConstantName1409); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 48, enumConstantName_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "enumConstantName"


    // $ANTLR start "typeName"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:466:1: typeName : ( Identifier | packageOrTypeName '.' Identifier );
    public final void typeName() throws RecognitionException {
        int typeName_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 49) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:467:2: ( Identifier | packageOrTypeName '.' Identifier )
            int alt66=2;
            int LA66_0 = input.LA(1);

            if ( (LA66_0==Identifier) ) {
                int LA66_1 = input.LA(2);

                if ( (LA66_1==EOF) ) {
                    alt66=1;
                }
                else if ( (LA66_1==28) ) {
                    alt66=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 66, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 66, 0, input);

                throw nvae;
            }
            switch (alt66) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:467:6: Identifier
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_typeName1425); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:468:9: packageOrTypeName '.' Identifier
                    {
                    pushFollow(FOLLOW_packageOrTypeName_in_typeName1435);
                    packageOrTypeName();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,28,FOLLOW_28_in_typeName1437); if (state.failed) return ;
                    match(input,Identifier,FOLLOW_Identifier_in_typeName1439); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 49, typeName_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "typeName"

    public static class type_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "type"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:471:1: type : ( Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )* ( '[' ']' )* | primitiveType ( '[' ']' )* );
    public final JavaParser.type_return type() throws RecognitionException {
        JavaParser.type_return retval = new JavaParser.type_return();
        retval.start = input.LT(1);
        int type_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 50) ) { return retval; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:472:2: ( Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )* ( '[' ']' )* | primitiveType ( '[' ']' )* )
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==Identifier) ) {
                alt72=1;
            }
            else if ( ((LA72_0>=55 && LA72_0<=62)) ) {
                alt72=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 72, 0, input);

                throw nvae;
            }
            switch (alt72) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:472:4: Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )* ( '[' ']' )*
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_type1450); if (state.failed) return retval;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:472:15: ( typeArguments )?
                    int alt67=2;
                    alt67 = dfa67.predict(input);
                    switch (alt67) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:472:16: typeArguments
                            {
                            pushFollow(FOLLOW_typeArguments_in_type1453);
                            typeArguments();

                            state._fsp--;
                            if (state.failed) return retval;

                            }
                            break;

                    }

                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:472:32: ( '.' Identifier ( typeArguments )? )*
                    loop69:
                    do {
                        int alt69=2;
                        alt69 = dfa69.predict(input);
                        switch (alt69) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:472:33: '.' Identifier ( typeArguments )?
                    	    {
                    	    match(input,28,FOLLOW_28_in_type1458); if (state.failed) return retval;
                    	    match(input,Identifier,FOLLOW_Identifier_in_type1460); if (state.failed) return retval;
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:472:48: ( typeArguments )?
                    	    int alt68=2;
                    	    alt68 = dfa68.predict(input);
                    	    switch (alt68) {
                    	        case 1 :
                    	            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:472:49: typeArguments
                    	            {
                    	            pushFollow(FOLLOW_typeArguments_in_type1463);
                    	            typeArguments();

                    	            state._fsp--;
                    	            if (state.failed) return retval;

                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop69;
                        }
                    } while (true);

                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:472:68: ( '[' ']' )*
                    loop70:
                    do {
                        int alt70=2;
                        alt70 = dfa70.predict(input);
                        switch (alt70) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:472:69: '[' ']'
                    	    {
                    	    match(input,41,FOLLOW_41_in_type1471); if (state.failed) return retval;
                    	    match(input,42,FOLLOW_42_in_type1473); if (state.failed) return retval;

                    	    }
                    	    break;

                    	default :
                    	    break loop70;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:473:4: primitiveType ( '[' ']' )*
                    {
                    pushFollow(FOLLOW_primitiveType_in_type1480);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return retval;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:473:18: ( '[' ']' )*
                    loop71:
                    do {
                        int alt71=2;
                        alt71 = dfa71.predict(input);
                        switch (alt71) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:473:19: '[' ']'
                    	    {
                    	    match(input,41,FOLLOW_41_in_type1483); if (state.failed) return retval;
                    	    match(input,42,FOLLOW_42_in_type1485); if (state.failed) return retval;

                    	    }
                    	    break;

                    	default :
                    	    break loop71;
                        }
                    } while (true);


                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 50, type_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "type"


    // $ANTLR start "primitiveType"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:476:1: primitiveType : ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' );
    public final void primitiveType() throws RecognitionException {
        int primitiveType_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 51) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:477:5: ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:
            {
            if ( (input.LA(1)>=55 && input.LA(1)<=62) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 51, primitiveType_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "primitiveType"

    public static class variableModifier_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "variableModifier"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:487:1: variableModifier : ( 'final' | annotation );
    public final JavaParser.variableModifier_return variableModifier() throws RecognitionException {
        JavaParser.variableModifier_return retval = new JavaParser.variableModifier_return();
        retval.start = input.LT(1);
        int variableModifier_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 52) ) { return retval; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:488:2: ( 'final' | annotation )
            int alt73=2;
            int LA73_0 = input.LA(1);

            if ( (LA73_0==49) ) {
                alt73=1;
            }
            else if ( (LA73_0==71) ) {
                alt73=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 73, 0, input);

                throw nvae;
            }
            switch (alt73) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:488:4: 'final'
                    {
                    match(input,49,FOLLOW_49_in_variableModifier1573); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:489:9: annotation
                    {
                    pushFollow(FOLLOW_annotation_in_variableModifier1583);
                    annotation();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 52, variableModifier_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "variableModifier"


    // $ANTLR start "typeArguments"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:492:1: typeArguments : '<' typeArgument ( ',' typeArgument )* '>' ;
    public final void typeArguments() throws RecognitionException {
        int typeArguments_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 53) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:493:2: ( '<' typeArgument ( ',' typeArgument )* '>' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:493:4: '<' typeArgument ( ',' typeArgument )* '>'
            {
            match(input,33,FOLLOW_33_in_typeArguments1594); if (state.failed) return ;
            pushFollow(FOLLOW_typeArgument_in_typeArguments1596);
            typeArgument();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:493:21: ( ',' typeArgument )*
            loop74:
            do {
                int alt74=2;
                int LA74_0 = input.LA(1);

                if ( (LA74_0==34) ) {
                    alt74=1;
                }


                switch (alt74) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:493:22: ',' typeArgument
            	    {
            	    match(input,34,FOLLOW_34_in_typeArguments1599); if (state.failed) return ;
            	    pushFollow(FOLLOW_typeArgument_in_typeArguments1601);
            	    typeArgument();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop74;
                }
            } while (true);

            match(input,35,FOLLOW_35_in_typeArguments1605); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 53, typeArguments_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "typeArguments"


    // $ANTLR start "typeArgument"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:496:1: typeArgument : ( type | '?' ( ( 'extends' | 'super' ) type )? );
    public final void typeArgument() throws RecognitionException {
        int typeArgument_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 54) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:497:2: ( type | '?' ( ( 'extends' | 'super' ) type )? )
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( (LA76_0==Identifier||(LA76_0>=55 && LA76_0<=62)) ) {
                alt76=1;
            }
            else if ( (LA76_0==63) ) {
                alt76=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 76, 0, input);

                throw nvae;
            }
            switch (alt76) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:497:4: type
                    {
                    pushFollow(FOLLOW_type_in_typeArgument1617);
                    type();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:498:4: '?' ( ( 'extends' | 'super' ) type )?
                    {
                    match(input,63,FOLLOW_63_in_typeArgument1622); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:498:8: ( ( 'extends' | 'super' ) type )?
                    int alt75=2;
                    int LA75_0 = input.LA(1);

                    if ( (LA75_0==31||LA75_0==64) ) {
                        alt75=1;
                    }
                    switch (alt75) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:498:9: ( 'extends' | 'super' ) type
                            {
                            if ( input.LA(1)==31||input.LA(1)==64 ) {
                                input.consume();
                                state.errorRecovery=false;state.failed=false;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return ;}
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }

                            pushFollow(FOLLOW_type_in_typeArgument1633);
                            type();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 54, typeArgument_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "typeArgument"


    // $ANTLR start "qualifiedNameList"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:501:1: qualifiedNameList : qualifiedName ( ',' qualifiedName )* ;
    public final void qualifiedNameList() throws RecognitionException {
        int qualifiedNameList_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 55) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:502:2: ( qualifiedName ( ',' qualifiedName )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:502:4: qualifiedName ( ',' qualifiedName )*
            {
            pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList1647);
            qualifiedName();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:502:18: ( ',' qualifiedName )*
            loop77:
            do {
                int alt77=2;
                int LA77_0 = input.LA(1);

                if ( (LA77_0==34) ) {
                    alt77=1;
                }


                switch (alt77) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:502:19: ',' qualifiedName
            	    {
            	    match(input,34,FOLLOW_34_in_qualifiedNameList1650); if (state.failed) return ;
            	    pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList1652);
            	    qualifiedName();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop77;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 55, qualifiedNameList_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "qualifiedNameList"


    // $ANTLR start "formalParameters"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:505:1: formalParameters : '(' ( formalParameterDecls )? ')' ;
    public final void formalParameters() throws RecognitionException {
        int formalParameters_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 56) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:506:2: ( '(' ( formalParameterDecls )? ')' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:506:4: '(' ( formalParameterDecls )? ')'
            {
            match(input,65,FOLLOW_65_in_formalParameters1666); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:506:8: ( formalParameterDecls )?
            int alt78=2;
            int LA78_0 = input.LA(1);

            if ( (LA78_0==Identifier||LA78_0==49||(LA78_0>=55 && LA78_0<=62)||LA78_0==71) ) {
                alt78=1;
            }
            switch (alt78) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: formalParameterDecls
                    {
                    pushFollow(FOLLOW_formalParameterDecls_in_formalParameters1668);
                    formalParameterDecls();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            match(input,66,FOLLOW_66_in_formalParameters1671); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 56, formalParameters_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "formalParameters"


    // $ANTLR start "formalParameterDecls"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:509:1: formalParameterDecls : ( variableModifier )* type ( formalParameterDeclsRest )? ;
    public final void formalParameterDecls() throws RecognitionException {
        int formalParameterDecls_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 57) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:510:2: ( ( variableModifier )* type ( formalParameterDeclsRest )? )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:510:4: ( variableModifier )* type ( formalParameterDeclsRest )?
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:510:4: ( variableModifier )*
            loop79:
            do {
                int alt79=2;
                int LA79_0 = input.LA(1);

                if ( (LA79_0==49||LA79_0==71) ) {
                    alt79=1;
                }


                switch (alt79) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: variableModifier
            	    {
            	    pushFollow(FOLLOW_variableModifier_in_formalParameterDecls1683);
            	    variableModifier();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop79;
                }
            } while (true);

            pushFollow(FOLLOW_type_in_formalParameterDecls1686);
            type();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:510:27: ( formalParameterDeclsRest )?
            int alt80=2;
            int LA80_0 = input.LA(1);

            if ( (LA80_0==Identifier||LA80_0==67) ) {
                alt80=1;
            }
            switch (alt80) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: formalParameterDeclsRest
                    {
                    pushFollow(FOLLOW_formalParameterDeclsRest_in_formalParameterDecls1688);
                    formalParameterDeclsRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 57, formalParameterDecls_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "formalParameterDecls"


    // $ANTLR start "formalParameterDeclsRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:513:1: formalParameterDeclsRest : ( variableDeclaratorId ( ',' formalParameterDecls )? | '...' variableDeclaratorId );
    public final void formalParameterDeclsRest() throws RecognitionException {
        int formalParameterDeclsRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 58) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:514:2: ( variableDeclaratorId ( ',' formalParameterDecls )? | '...' variableDeclaratorId )
            int alt82=2;
            int LA82_0 = input.LA(1);

            if ( (LA82_0==Identifier) ) {
                alt82=1;
            }
            else if ( (LA82_0==67) ) {
                alt82=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 82, 0, input);

                throw nvae;
            }
            switch (alt82) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:514:4: variableDeclaratorId ( ',' formalParameterDecls )?
                    {
                    pushFollow(FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest1701);
                    variableDeclaratorId();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:514:25: ( ',' formalParameterDecls )?
                    int alt81=2;
                    int LA81_0 = input.LA(1);

                    if ( (LA81_0==34) ) {
                        alt81=1;
                    }
                    switch (alt81) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:514:26: ',' formalParameterDecls
                            {
                            match(input,34,FOLLOW_34_in_formalParameterDeclsRest1704); if (state.failed) return ;
                            pushFollow(FOLLOW_formalParameterDecls_in_formalParameterDeclsRest1706);
                            formalParameterDecls();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:515:6: '...' variableDeclaratorId
                    {
                    match(input,67,FOLLOW_67_in_formalParameterDeclsRest1715); if (state.failed) return ;
                    pushFollow(FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest1717);
                    variableDeclaratorId();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 58, formalParameterDeclsRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "formalParameterDeclsRest"


    // $ANTLR start "methodBody"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:518:1: methodBody : block ;
    public final void methodBody() throws RecognitionException {
        int methodBody_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 59) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:519:2: ( block )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:519:4: block
            {
            pushFollow(FOLLOW_block_in_methodBody1729);
            block();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 59, methodBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "methodBody"


    // $ANTLR start "qualifiedName"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:522:1: qualifiedName : Identifier ( '.' Identifier )* ;
    public final void qualifiedName() throws RecognitionException {
        int qualifiedName_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 60) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:523:2: ( Identifier ( '.' Identifier )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:523:4: Identifier ( '.' Identifier )*
            {
            match(input,Identifier,FOLLOW_Identifier_in_qualifiedName1740); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:523:15: ( '.' Identifier )*
            loop83:
            do {
                int alt83=2;
                int LA83_0 = input.LA(1);

                if ( (LA83_0==28) ) {
                    alt83=1;
                }


                switch (alt83) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:523:16: '.' Identifier
            	    {
            	    match(input,28,FOLLOW_28_in_qualifiedName1743); if (state.failed) return ;
            	    match(input,Identifier,FOLLOW_Identifier_in_qualifiedName1745); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop83;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 60, qualifiedName_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "qualifiedName"


    // $ANTLR start "literal"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:526:1: literal : ( integerLiteral | FloatingPointLiteral | CharacterLiteral | StringLiteral | booleanLiteral | 'null' );
    public final void literal() throws RecognitionException {
        int literal_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 61) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:527:2: ( integerLiteral | FloatingPointLiteral | CharacterLiteral | StringLiteral | booleanLiteral | 'null' )
            int alt84=6;
            switch ( input.LA(1) ) {
            case HexLiteral:
            case OctalLiteral:
            case DecimalLiteral:
                {
                alt84=1;
                }
                break;
            case FloatingPointLiteral:
                {
                alt84=2;
                }
                break;
            case CharacterLiteral:
                {
                alt84=3;
                }
                break;
            case StringLiteral:
                {
                alt84=4;
                }
                break;
            case 69:
            case 70:
                {
                alt84=5;
                }
                break;
            case 68:
                {
                alt84=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 84, 0, input);

                throw nvae;
            }

            switch (alt84) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:527:6: integerLiteral
                    {
                    pushFollow(FOLLOW_integerLiteral_in_literal1762);
                    integerLiteral();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:528:9: FloatingPointLiteral
                    {
                    match(input,FloatingPointLiteral,FOLLOW_FloatingPointLiteral_in_literal1772); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:529:9: CharacterLiteral
                    {
                    match(input,CharacterLiteral,FOLLOW_CharacterLiteral_in_literal1782); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:530:9: StringLiteral
                    {
                    match(input,StringLiteral,FOLLOW_StringLiteral_in_literal1792); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:531:9: booleanLiteral
                    {
                    pushFollow(FOLLOW_booleanLiteral_in_literal1802);
                    booleanLiteral();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:532:9: 'null'
                    {
                    match(input,68,FOLLOW_68_in_literal1812); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 61, literal_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "literal"


    // $ANTLR start "integerLiteral"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:535:1: integerLiteral : ( HexLiteral | OctalLiteral | DecimalLiteral );
    public final void integerLiteral() throws RecognitionException {
        int integerLiteral_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 62) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:536:5: ( HexLiteral | OctalLiteral | DecimalLiteral )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:
            {
            if ( (input.LA(1)>=HexLiteral && input.LA(1)<=DecimalLiteral) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 62, integerLiteral_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "integerLiteral"


    // $ANTLR start "booleanLiteral"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:541:1: booleanLiteral : ( 'true' | 'false' );
    public final void booleanLiteral() throws RecognitionException {
        int booleanLiteral_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 63) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:542:5: ( 'true' | 'false' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:
            {
            if ( (input.LA(1)>=69 && input.LA(1)<=70) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 63, booleanLiteral_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "booleanLiteral"


    // $ANTLR start "annotations"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:548:1: annotations : ( annotation )+ ;
    public final void annotations() throws RecognitionException {
        int annotations_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 64) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:549:2: ( ( annotation )+ )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:549:4: ( annotation )+
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:549:4: ( annotation )+
            int cnt85=0;
            loop85:
            do {
                int alt85=2;
                alt85 = dfa85.predict(input);
                switch (alt85) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: annotation
            	    {
            	    pushFollow(FOLLOW_annotation_in_annotations1893);
            	    annotation();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt85 >= 1 ) break loop85;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(85, input);
                        throw eee;
                }
                cnt85++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 64, annotations_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotations"


    // $ANTLR start "annotation"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:552:1: annotation : '@' annotationName ( '(' ( elementValuePairs )? ')' )? ;
    public final void annotation() throws RecognitionException {
        int annotation_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 65) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:553:2: ( '@' annotationName ( '(' ( elementValuePairs )? ')' )? )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:553:4: '@' annotationName ( '(' ( elementValuePairs )? ')' )?
            {
            match(input,71,FOLLOW_71_in_annotation1905); if (state.failed) return ;
            pushFollow(FOLLOW_annotationName_in_annotation1907);
            annotationName();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:553:23: ( '(' ( elementValuePairs )? ')' )?
            int alt87=2;
            alt87 = dfa87.predict(input);
            switch (alt87) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:553:24: '(' ( elementValuePairs )? ')'
                    {
                    match(input,65,FOLLOW_65_in_annotation1910); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:553:28: ( elementValuePairs )?
                    int alt86=2;
                    alt86 = dfa86.predict(input);
                    switch (alt86) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: elementValuePairs
                            {
                            pushFollow(FOLLOW_elementValuePairs_in_annotation1912);
                            elementValuePairs();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,66,FOLLOW_66_in_annotation1915); if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 65, annotation_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotation"


    // $ANTLR start "annotationName"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:556:1: annotationName : Identifier ( '.' Identifier )* ;
    public final void annotationName() throws RecognitionException {
        int annotationName_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 66) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:557:2: ( Identifier ( '.' Identifier )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:557:4: Identifier ( '.' Identifier )*
            {
            match(input,Identifier,FOLLOW_Identifier_in_annotationName1929); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:557:15: ( '.' Identifier )*
            loop88:
            do {
                int alt88=2;
                alt88 = dfa88.predict(input);
                switch (alt88) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:557:16: '.' Identifier
            	    {
            	    match(input,28,FOLLOW_28_in_annotationName1932); if (state.failed) return ;
            	    match(input,Identifier,FOLLOW_Identifier_in_annotationName1934); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop88;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 66, annotationName_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationName"


    // $ANTLR start "elementValuePairs"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:560:1: elementValuePairs : elementValuePair ( ',' elementValuePair )* ;
    public final void elementValuePairs() throws RecognitionException {
        int elementValuePairs_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 67) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:561:2: ( elementValuePair ( ',' elementValuePair )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:561:4: elementValuePair ( ',' elementValuePair )*
            {
            pushFollow(FOLLOW_elementValuePair_in_elementValuePairs1948);
            elementValuePair();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:561:21: ( ',' elementValuePair )*
            loop89:
            do {
                int alt89=2;
                int LA89_0 = input.LA(1);

                if ( (LA89_0==34) ) {
                    alt89=1;
                }


                switch (alt89) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:561:22: ',' elementValuePair
            	    {
            	    match(input,34,FOLLOW_34_in_elementValuePairs1951); if (state.failed) return ;
            	    pushFollow(FOLLOW_elementValuePair_in_elementValuePairs1953);
            	    elementValuePair();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop89;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 67, elementValuePairs_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "elementValuePairs"


    // $ANTLR start "elementValuePair"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:564:1: elementValuePair : ( Identifier '=' )? elementValue ;
    public final void elementValuePair() throws RecognitionException {
        int elementValuePair_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 68) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:565:2: ( ( Identifier '=' )? elementValue )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:565:4: ( Identifier '=' )? elementValue
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:565:4: ( Identifier '=' )?
            int alt90=2;
            alt90 = dfa90.predict(input);
            switch (alt90) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:565:5: Identifier '='
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_elementValuePair1968); if (state.failed) return ;
                    match(input,44,FOLLOW_44_in_elementValuePair1970); if (state.failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_elementValue_in_elementValuePair1974);
            elementValue();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 68, elementValuePair_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "elementValuePair"


    // $ANTLR start "elementValue"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:568:1: elementValue : ( conditionalExpression | annotation | elementValueArrayInitializer );
    public final void elementValue() throws RecognitionException {
        int elementValue_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 69) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:569:2: ( conditionalExpression | annotation | elementValueArrayInitializer )
            int alt91=3;
            alt91 = dfa91.predict(input);
            switch (alt91) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:569:4: conditionalExpression
                    {
                    pushFollow(FOLLOW_conditionalExpression_in_elementValue1986);
                    conditionalExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:570:6: annotation
                    {
                    pushFollow(FOLLOW_annotation_in_elementValue1993);
                    annotation();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:571:6: elementValueArrayInitializer
                    {
                    pushFollow(FOLLOW_elementValueArrayInitializer_in_elementValue2000);
                    elementValueArrayInitializer();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 69, elementValue_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "elementValue"


    // $ANTLR start "elementValueArrayInitializer"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:574:1: elementValueArrayInitializer : '{' ( elementValue ( ',' elementValue )* )? '}' ;
    public final void elementValueArrayInitializer() throws RecognitionException {
        int elementValueArrayInitializer_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 70) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:575:2: ( '{' ( elementValue ( ',' elementValue )* )? '}' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:575:4: '{' ( elementValue ( ',' elementValue )* )? '}'
            {
            match(input,37,FOLLOW_37_in_elementValueArrayInitializer2012); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:575:8: ( elementValue ( ',' elementValue )* )?
            int alt93=2;
            alt93 = dfa93.predict(input);
            switch (alt93) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:575:9: elementValue ( ',' elementValue )*
                    {
                    pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer2015);
                    elementValue();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:575:22: ( ',' elementValue )*
                    loop92:
                    do {
                        int alt92=2;
                        int LA92_0 = input.LA(1);

                        if ( (LA92_0==34) ) {
                            alt92=1;
                        }


                        switch (alt92) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:575:23: ',' elementValue
                    	    {
                    	    match(input,34,FOLLOW_34_in_elementValueArrayInitializer2018); if (state.failed) return ;
                    	    pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer2020);
                    	    elementValue();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop92;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,38,FOLLOW_38_in_elementValueArrayInitializer2027); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 70, elementValueArrayInitializer_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "elementValueArrayInitializer"


    // $ANTLR start "annotationTypeDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:578:1: annotationTypeDeclaration : '@' 'interface' Identifier annotationTypeBody ;
    public final void annotationTypeDeclaration() throws RecognitionException {
        int annotationTypeDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 71) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:579:2: ( '@' 'interface' Identifier annotationTypeBody )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:579:4: '@' 'interface' Identifier annotationTypeBody
            {
            match(input,71,FOLLOW_71_in_annotationTypeDeclaration2039); if (state.failed) return ;
            match(input,39,FOLLOW_39_in_annotationTypeDeclaration2041); if (state.failed) return ;
            match(input,Identifier,FOLLOW_Identifier_in_annotationTypeDeclaration2043); if (state.failed) return ;
            pushFollow(FOLLOW_annotationTypeBody_in_annotationTypeDeclaration2045);
            annotationTypeBody();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 71, annotationTypeDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationTypeDeclaration"


    // $ANTLR start "annotationTypeBody"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:582:1: annotationTypeBody : '{' ( annotationTypeElementDeclarations )? '}' ;
    public final void annotationTypeBody() throws RecognitionException {
        int annotationTypeBody_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 72) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:583:2: ( '{' ( annotationTypeElementDeclarations )? '}' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:583:4: '{' ( annotationTypeElementDeclarations )? '}'
            {
            match(input,37,FOLLOW_37_in_annotationTypeBody2057); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:583:8: ( annotationTypeElementDeclarations )?
            int alt94=2;
            alt94 = dfa94.predict(input);
            switch (alt94) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:583:9: annotationTypeElementDeclarations
                    {
                    pushFollow(FOLLOW_annotationTypeElementDeclarations_in_annotationTypeBody2060);
                    annotationTypeElementDeclarations();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            match(input,38,FOLLOW_38_in_annotationTypeBody2064); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 72, annotationTypeBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationTypeBody"


    // $ANTLR start "annotationTypeElementDeclarations"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:586:1: annotationTypeElementDeclarations : ( annotationTypeElementDeclaration ) ( annotationTypeElementDeclaration )* ;
    public final void annotationTypeElementDeclarations() throws RecognitionException {
        int annotationTypeElementDeclarations_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 73) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:587:2: ( ( annotationTypeElementDeclaration ) ( annotationTypeElementDeclaration )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:587:4: ( annotationTypeElementDeclaration ) ( annotationTypeElementDeclaration )*
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:587:4: ( annotationTypeElementDeclaration )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:587:5: annotationTypeElementDeclaration
            {
            pushFollow(FOLLOW_annotationTypeElementDeclaration_in_annotationTypeElementDeclarations2077);
            annotationTypeElementDeclaration();

            state._fsp--;
            if (state.failed) return ;

            }

            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:587:39: ( annotationTypeElementDeclaration )*
            loop95:
            do {
                int alt95=2;
                alt95 = dfa95.predict(input);
                switch (alt95) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:587:40: annotationTypeElementDeclaration
            	    {
            	    pushFollow(FOLLOW_annotationTypeElementDeclaration_in_annotationTypeElementDeclarations2081);
            	    annotationTypeElementDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop95;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 73, annotationTypeElementDeclarations_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationTypeElementDeclarations"


    // $ANTLR start "annotationTypeElementDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:590:1: annotationTypeElementDeclaration : ( modifier )* annotationTypeElementRest ;
    public final void annotationTypeElementDeclaration() throws RecognitionException {
        int annotationTypeElementDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 74) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:591:2: ( ( modifier )* annotationTypeElementRest )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:591:4: ( modifier )* annotationTypeElementRest
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:591:4: ( modifier )*
            loop96:
            do {
                int alt96=2;
                alt96 = dfa96.predict(input);
                switch (alt96) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:591:5: modifier
            	    {
            	    pushFollow(FOLLOW_modifier_in_annotationTypeElementDeclaration2096);
            	    modifier();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop96;
                }
            } while (true);

            pushFollow(FOLLOW_annotationTypeElementRest_in_annotationTypeElementDeclaration2100);
            annotationTypeElementRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 74, annotationTypeElementDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationTypeElementDeclaration"


    // $ANTLR start "annotationTypeElementRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:594:1: annotationTypeElementRest : ( type annotationMethodOrConstantRest ';' | classDeclaration ( ';' )? | interfaceDeclaration ( ';' )? | enumDeclaration ( ';' )? | annotationTypeDeclaration ( ';' )? );
    public final void annotationTypeElementRest() throws RecognitionException {
        int annotationTypeElementRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 75) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:595:2: ( type annotationMethodOrConstantRest ';' | classDeclaration ( ';' )? | interfaceDeclaration ( ';' )? | enumDeclaration ( ';' )? | annotationTypeDeclaration ( ';' )? )
            int alt101=5;
            alt101 = dfa101.predict(input);
            switch (alt101) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:595:4: type annotationMethodOrConstantRest ';'
                    {
                    pushFollow(FOLLOW_type_in_annotationTypeElementRest2112);
                    type();

                    state._fsp--;
                    if (state.failed) return ;
                    pushFollow(FOLLOW_annotationMethodOrConstantRest_in_annotationTypeElementRest2114);
                    annotationMethodOrConstantRest();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,25,FOLLOW_25_in_annotationTypeElementRest2116); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:596:6: classDeclaration ( ';' )?
                    {
                    pushFollow(FOLLOW_classDeclaration_in_annotationTypeElementRest2123);
                    classDeclaration();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:596:23: ( ';' )?
                    int alt97=2;
                    alt97 = dfa97.predict(input);
                    switch (alt97) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: ';'
                            {
                            match(input,25,FOLLOW_25_in_annotationTypeElementRest2125); if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:597:6: interfaceDeclaration ( ';' )?
                    {
                    pushFollow(FOLLOW_interfaceDeclaration_in_annotationTypeElementRest2133);
                    interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:597:27: ( ';' )?
                    int alt98=2;
                    alt98 = dfa98.predict(input);
                    switch (alt98) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: ';'
                            {
                            match(input,25,FOLLOW_25_in_annotationTypeElementRest2135); if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:598:6: enumDeclaration ( ';' )?
                    {
                    pushFollow(FOLLOW_enumDeclaration_in_annotationTypeElementRest2143);
                    enumDeclaration();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:598:22: ( ';' )?
                    int alt99=2;
                    alt99 = dfa99.predict(input);
                    switch (alt99) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: ';'
                            {
                            match(input,25,FOLLOW_25_in_annotationTypeElementRest2145); if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 5 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:599:6: annotationTypeDeclaration ( ';' )?
                    {
                    pushFollow(FOLLOW_annotationTypeDeclaration_in_annotationTypeElementRest2153);
                    annotationTypeDeclaration();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:599:32: ( ';' )?
                    int alt100=2;
                    alt100 = dfa100.predict(input);
                    switch (alt100) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: ';'
                            {
                            match(input,25,FOLLOW_25_in_annotationTypeElementRest2155); if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 75, annotationTypeElementRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationTypeElementRest"


    // $ANTLR start "annotationMethodOrConstantRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:602:1: annotationMethodOrConstantRest : ( annotationMethodRest | annotationConstantRest );
    public final void annotationMethodOrConstantRest() throws RecognitionException {
        int annotationMethodOrConstantRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 76) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:603:2: ( annotationMethodRest | annotationConstantRest )
            int alt102=2;
            int LA102_0 = input.LA(1);

            if ( (LA102_0==Identifier) ) {
                int LA102_1 = input.LA(2);

                if ( (LA102_1==65) ) {
                    alt102=1;
                }
                else if ( (LA102_1==25||LA102_1==34||LA102_1==41||LA102_1==44) ) {
                    alt102=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 102, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 102, 0, input);

                throw nvae;
            }
            switch (alt102) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:603:4: annotationMethodRest
                    {
                    pushFollow(FOLLOW_annotationMethodRest_in_annotationMethodOrConstantRest2168);
                    annotationMethodRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:604:6: annotationConstantRest
                    {
                    pushFollow(FOLLOW_annotationConstantRest_in_annotationMethodOrConstantRest2175);
                    annotationConstantRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 76, annotationMethodOrConstantRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationMethodOrConstantRest"


    // $ANTLR start "annotationMethodRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:607:1: annotationMethodRest : Identifier '(' ')' ( defaultValue )? ;
    public final void annotationMethodRest() throws RecognitionException {
        int annotationMethodRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 77) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:608:3: ( Identifier '(' ')' ( defaultValue )? )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:608:5: Identifier '(' ')' ( defaultValue )?
            {
            match(input,Identifier,FOLLOW_Identifier_in_annotationMethodRest2188); if (state.failed) return ;
            match(input,65,FOLLOW_65_in_annotationMethodRest2190); if (state.failed) return ;
            match(input,66,FOLLOW_66_in_annotationMethodRest2192); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:608:24: ( defaultValue )?
            int alt103=2;
            int LA103_0 = input.LA(1);

            if ( (LA103_0==72) ) {
                alt103=1;
            }
            switch (alt103) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:608:25: defaultValue
                    {
                    pushFollow(FOLLOW_defaultValue_in_annotationMethodRest2195);
                    defaultValue();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 77, annotationMethodRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationMethodRest"


    // $ANTLR start "annotationConstantRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:611:1: annotationConstantRest : variableDeclarators ;
    public final void annotationConstantRest() throws RecognitionException {
        int annotationConstantRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 78) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:612:3: ( variableDeclarators )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:612:5: variableDeclarators
            {
            pushFollow(FOLLOW_variableDeclarators_in_annotationConstantRest2212);
            variableDeclarators();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 78, annotationConstantRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationConstantRest"


    // $ANTLR start "defaultValue"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:615:1: defaultValue : 'default' elementValue ;
    public final void defaultValue() throws RecognitionException {
        int defaultValue_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 79) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:616:3: ( 'default' elementValue )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:616:5: 'default' elementValue
            {
            match(input,72,FOLLOW_72_in_defaultValue2227); if (state.failed) return ;
            pushFollow(FOLLOW_elementValue_in_defaultValue2229);
            elementValue();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 79, defaultValue_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "defaultValue"


    // $ANTLR start "block"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:621:1: block : '{' ( blockStatement )* '}' ;
    public final void block() throws RecognitionException {
        int block_StartIndex = input.index();

                    this.localVariableLevel++;
                
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 80) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:628:2: ( '{' ( blockStatement )* '}' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:628:4: '{' ( blockStatement )* '}'
            {
            match(input,37,FOLLOW_37_in_block2269); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:628:8: ( blockStatement )*
            loop104:
            do {
                int alt104=2;
                alt104 = dfa104.predict(input);
                switch (alt104) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: blockStatement
            	    {
            	    pushFollow(FOLLOW_blockStatement_in_block2271);
            	    blockStatement();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop104;
                }
            } while (true);

            match(input,38,FOLLOW_38_in_block2274); if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {

                          this.localVariableLevel--;
                      
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 80, block_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "block"


    // $ANTLR start "blockStatement"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:631:1: blockStatement : ( localVariableDeclaration | classOrInterfaceDeclaration | statement );
    public final void blockStatement() throws RecognitionException {
        int blockStatement_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 81) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:632:2: ( localVariableDeclaration | classOrInterfaceDeclaration | statement )
            int alt105=3;
            alt105 = dfa105.predict(input);
            switch (alt105) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:632:4: localVariableDeclaration
                    {
                    pushFollow(FOLLOW_localVariableDeclaration_in_blockStatement2286);
                    localVariableDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:633:4: classOrInterfaceDeclaration
                    {
                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_blockStatement2291);
                    classOrInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:634:8: statement
                    {
                    pushFollow(FOLLOW_statement_in_blockStatement2300);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 81, blockStatement_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "blockStatement"

    protected static class localVariableDeclaration_scope {
        JavaLocalDeclarationDescr descr;
    }
    protected Stack localVariableDeclaration_stack = new Stack();


    // $ANTLR start "localVariableDeclaration"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:637:1: localVariableDeclaration : ( variableModifier )* type variableDeclarators ';' ;
    public final void localVariableDeclaration() throws RecognitionException {
        localVariableDeclaration_stack.push(new localVariableDeclaration_scope());
        int localVariableDeclaration_StartIndex = input.index();
        JavaParser.variableModifier_return variableModifier1 = null;

        JavaParser.type_return type2 = null;



                    ((localVariableDeclaration_scope)localVariableDeclaration_stack.peek()).descr = new JavaLocalDeclarationDescr();
                
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 82) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:647:2: ( ( variableModifier )* type variableDeclarators ';' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:648:2: ( variableModifier )* type variableDeclarators ';'
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:648:2: ( variableModifier )*
            loop106:
            do {
                int alt106=2;
                int LA106_0 = input.LA(1);

                if ( (LA106_0==49||LA106_0==71) ) {
                    alt106=1;
                }


                switch (alt106) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:648:4: variableModifier
            	    {
            	    pushFollow(FOLLOW_variableModifier_in_localVariableDeclaration2348);
            	    variableModifier1=variableModifier();

            	    state._fsp--;
            	    if (state.failed) return ;
            	    if ( state.backtracking==0 ) {
            	       
            	      	        ((localVariableDeclaration_scope)localVariableDeclaration_stack.peek()).descr.updateStart( ((CommonToken)(variableModifier1!=null?((Token)variableModifier1.start):null)).getStartIndex() - 1 );
            	      	        ((localVariableDeclaration_scope)localVariableDeclaration_stack.peek()).descr.addModifier( (variableModifier1!=null?input.toString(variableModifier1.start,variableModifier1.stop):null) );
            	      	    
            	    }

            	    }
            	    break;

            	default :
            	    break loop106;
                }
            } while (true);

            pushFollow(FOLLOW_type_in_localVariableDeclaration2365);
            type2=type();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               
              	        ((localVariableDeclaration_scope)localVariableDeclaration_stack.peek()).descr.updateStart( ((CommonToken)(type2!=null?((Token)type2.start):null)).getStartIndex() - 1 );
              	        ((localVariableDeclaration_scope)localVariableDeclaration_stack.peek()).descr.setType( (type2!=null?input.toString(type2.start,type2.stop):null) );
              	        ((localVariableDeclaration_scope)localVariableDeclaration_stack.peek()).descr.setEnd( ((CommonToken)(type2!=null?((Token)type2.stop):null)).getStopIndex() );
              	    
            }
            pushFollow(FOLLOW_variableDeclarators_in_localVariableDeclaration2376);
            variableDeclarators();

            state._fsp--;
            if (state.failed) return ;
            match(input,25,FOLLOW_25_in_localVariableDeclaration2378); if (state.failed) return ;

            }

            if ( state.backtracking==0 ) {

                          localDeclarations.add( ((localVariableDeclaration_scope)localVariableDeclaration_stack.peek()).descr );
                      
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 82, localVariableDeclaration_StartIndex); }
            localVariableDeclaration_stack.pop();
        }
        return ;
    }
    // $ANTLR end "localVariableDeclaration"


    // $ANTLR start "statement"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:663:1: statement : ( block | 'assert' expression ( ':' expression )? ';' | 'if' parExpression statement ( options {k=1; } : 'else' statement )? | 'for' '(' forControl ')' statement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | 'try' block ( catches 'finally' block | catches | 'finally' block ) | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( Identifier )? ';' | 'continue' ( Identifier )? ';' | modifyStatement | ';' | statementExpression ';' | Identifier ':' statement );
    public final void statement() throws RecognitionException {
        int statement_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 83) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:664:2: ( block | 'assert' expression ( ':' expression )? ';' | 'if' parExpression statement ( options {k=1; } : 'else' statement )? | 'for' '(' forControl ')' statement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | 'try' block ( catches 'finally' block | catches | 'finally' block ) | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( Identifier )? ';' | 'continue' ( Identifier )? ';' | modifyStatement | ';' | statementExpression ';' | Identifier ':' statement )
            int alt113=17;
            alt113 = dfa113.predict(input);
            switch (alt113) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:664:4: block
                    {
                    pushFollow(FOLLOW_block_in_statement2390);
                    block();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:665:7: 'assert' expression ( ':' expression )? ';'
                    {
                    match(input,73,FOLLOW_73_in_statement2398); if (state.failed) return ;
                    pushFollow(FOLLOW_expression_in_statement2400);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:665:27: ( ':' expression )?
                    int alt107=2;
                    int LA107_0 = input.LA(1);

                    if ( (LA107_0==74) ) {
                        alt107=1;
                    }
                    switch (alt107) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:665:28: ':' expression
                            {
                            match(input,74,FOLLOW_74_in_statement2403); if (state.failed) return ;
                            pushFollow(FOLLOW_expression_in_statement2405);
                            expression();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,25,FOLLOW_25_in_statement2409); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:666:7: 'if' parExpression statement ( options {k=1; } : 'else' statement )?
                    {
                    match(input,75,FOLLOW_75_in_statement2417); if (state.failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement2419);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;
                    pushFollow(FOLLOW_statement_in_statement2421);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:666:36: ( options {k=1; } : 'else' statement )?
                    int alt108=2;
                    int LA108_0 = input.LA(1);

                    if ( (LA108_0==76) ) {
                        int LA108_1 = input.LA(2);

                        if ( (synpred150_Java()) ) {
                            alt108=1;
                        }
                    }
                    switch (alt108) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:666:52: 'else' statement
                            {
                            match(input,76,FOLLOW_76_in_statement2431); if (state.failed) return ;
                            pushFollow(FOLLOW_statement_in_statement2433);
                            statement();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:667:7: 'for' '(' forControl ')' statement
                    {
                    match(input,77,FOLLOW_77_in_statement2443); if (state.failed) return ;
                    match(input,65,FOLLOW_65_in_statement2445); if (state.failed) return ;
                    pushFollow(FOLLOW_forControl_in_statement2447);
                    forControl();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,66,FOLLOW_66_in_statement2449); if (state.failed) return ;
                    pushFollow(FOLLOW_statement_in_statement2451);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:668:7: 'while' parExpression statement
                    {
                    match(input,78,FOLLOW_78_in_statement2459); if (state.failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement2461);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;
                    pushFollow(FOLLOW_statement_in_statement2463);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:669:7: 'do' statement 'while' parExpression ';'
                    {
                    match(input,79,FOLLOW_79_in_statement2471); if (state.failed) return ;
                    pushFollow(FOLLOW_statement_in_statement2473);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,78,FOLLOW_78_in_statement2475); if (state.failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement2477);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,25,FOLLOW_25_in_statement2479); if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:670:7: 'try' block ( catches 'finally' block | catches | 'finally' block )
                    {
                    match(input,80,FOLLOW_80_in_statement2487); if (state.failed) return ;
                    pushFollow(FOLLOW_block_in_statement2489);
                    block();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:671:7: ( catches 'finally' block | catches | 'finally' block )
                    int alt109=3;
                    int LA109_0 = input.LA(1);

                    if ( (LA109_0==93) ) {
                        int LA109_1 = input.LA(2);

                        if ( (LA109_1==65) ) {
                            int LA109_3 = input.LA(3);

                            if ( (synpred155_Java()) ) {
                                alt109=1;
                            }
                            else if ( (synpred156_Java()) ) {
                                alt109=2;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return ;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 109, 3, input);

                                throw nvae;
                            }
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 109, 1, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA109_0==81) ) {
                        alt109=3;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 109, 0, input);

                        throw nvae;
                    }
                    switch (alt109) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:671:9: catches 'finally' block
                            {
                            pushFollow(FOLLOW_catches_in_statement2499);
                            catches();

                            state._fsp--;
                            if (state.failed) return ;
                            match(input,81,FOLLOW_81_in_statement2501); if (state.failed) return ;
                            pushFollow(FOLLOW_block_in_statement2503);
                            block();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;
                        case 2 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:672:9: catches
                            {
                            pushFollow(FOLLOW_catches_in_statement2513);
                            catches();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;
                        case 3 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:673:9: 'finally' block
                            {
                            match(input,81,FOLLOW_81_in_statement2523); if (state.failed) return ;
                            pushFollow(FOLLOW_block_in_statement2525);
                            block();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 8 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:675:7: 'switch' parExpression '{' switchBlockStatementGroups '}'
                    {
                    match(input,82,FOLLOW_82_in_statement2541); if (state.failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement2543);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,37,FOLLOW_37_in_statement2545); if (state.failed) return ;
                    pushFollow(FOLLOW_switchBlockStatementGroups_in_statement2547);
                    switchBlockStatementGroups();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,38,FOLLOW_38_in_statement2549); if (state.failed) return ;

                    }
                    break;
                case 9 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:676:7: 'synchronized' parExpression block
                    {
                    match(input,51,FOLLOW_51_in_statement2557); if (state.failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement2559);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;
                    pushFollow(FOLLOW_block_in_statement2561);
                    block();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 10 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:677:7: 'return' ( expression )? ';'
                    {
                    match(input,83,FOLLOW_83_in_statement2569); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:677:16: ( expression )?
                    int alt110=2;
                    alt110 = dfa110.predict(input);
                    switch (alt110) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: expression
                            {
                            pushFollow(FOLLOW_expression_in_statement2571);
                            expression();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,25,FOLLOW_25_in_statement2574); if (state.failed) return ;

                    }
                    break;
                case 11 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:678:7: 'throw' expression ';'
                    {
                    match(input,84,FOLLOW_84_in_statement2582); if (state.failed) return ;
                    pushFollow(FOLLOW_expression_in_statement2584);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,25,FOLLOW_25_in_statement2586); if (state.failed) return ;

                    }
                    break;
                case 12 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:679:7: 'break' ( Identifier )? ';'
                    {
                    match(input,85,FOLLOW_85_in_statement2594); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:679:15: ( Identifier )?
                    int alt111=2;
                    int LA111_0 = input.LA(1);

                    if ( (LA111_0==Identifier) ) {
                        alt111=1;
                    }
                    switch (alt111) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: Identifier
                            {
                            match(input,Identifier,FOLLOW_Identifier_in_statement2596); if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,25,FOLLOW_25_in_statement2599); if (state.failed) return ;

                    }
                    break;
                case 13 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:680:7: 'continue' ( Identifier )? ';'
                    {
                    match(input,86,FOLLOW_86_in_statement2607); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:680:18: ( Identifier )?
                    int alt112=2;
                    int LA112_0 = input.LA(1);

                    if ( (LA112_0==Identifier) ) {
                        alt112=1;
                    }
                    switch (alt112) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: Identifier
                            {
                            match(input,Identifier,FOLLOW_Identifier_in_statement2609); if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,25,FOLLOW_25_in_statement2612); if (state.failed) return ;

                    }
                    break;
                case 14 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:682:7: modifyStatement
                    {
                    pushFollow(FOLLOW_modifyStatement_in_statement2625);
                    modifyStatement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 15 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:683:7: ';'
                    {
                    match(input,25,FOLLOW_25_in_statement2633); if (state.failed) return ;

                    }
                    break;
                case 16 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:684:7: statementExpression ';'
                    {
                    pushFollow(FOLLOW_statementExpression_in_statement2641);
                    statementExpression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,25,FOLLOW_25_in_statement2643); if (state.failed) return ;

                    }
                    break;
                case 17 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:685:7: Identifier ':' statement
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_statement2651); if (state.failed) return ;
                    match(input,74,FOLLOW_74_in_statement2653); if (state.failed) return ;
                    pushFollow(FOLLOW_statement_in_statement2655);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 83, statement_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "statement"


    // $ANTLR start "modifyStatement"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:688:1: modifyStatement : s= 'modify' parExpression '{' (e= expression ( ',' e= expression )* )? c= '}' ;
    public final void modifyStatement() throws RecognitionException {
        int modifyStatement_StartIndex = input.index();
        Token s=null;
        Token c=null;
        JavaParser.expression_return e = null;

        JavaParser.parExpression_return parExpression3 = null;



        	    JavaModifyBlockDescr d = null;
        	
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 84) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:692:2: (s= 'modify' parExpression '{' (e= expression ( ',' e= expression )* )? c= '}' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:692:4: s= 'modify' parExpression '{' (e= expression ( ',' e= expression )* )? c= '}'
            {
            s=(Token)match(input,87,FOLLOW_87_in_modifyStatement2675); if (state.failed) return ;
            pushFollow(FOLLOW_parExpression_in_modifyStatement2677);
            parExpression3=parExpression();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {

              	    d = new JavaModifyBlockDescr( (parExpression3!=null?input.toString(parExpression3.start,parExpression3.stop):null) );
              	    d.setStart( ((CommonToken)s).getStartIndex() );
              	    this.blocks.add( d );
              	    
              	
            }
            match(input,37,FOLLOW_37_in_modifyStatement2684); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:699:6: (e= expression ( ',' e= expression )* )?
            int alt115=2;
            alt115 = dfa115.predict(input);
            switch (alt115) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:699:8: e= expression ( ',' e= expression )*
                    {
                    pushFollow(FOLLOW_expression_in_modifyStatement2692);
                    e=expression();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                       d.getExpressions().add( (e!=null?input.toString(e.start,e.stop):null) );
                    }
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:700:9: ( ',' e= expression )*
                    loop114:
                    do {
                        int alt114=2;
                        int LA114_0 = input.LA(1);

                        if ( (LA114_0==34) ) {
                            alt114=1;
                        }


                        switch (alt114) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:700:10: ',' e= expression
                    	    {
                    	    match(input,34,FOLLOW_34_in_modifyStatement2705); if (state.failed) return ;
                    	    pushFollow(FOLLOW_expression_in_modifyStatement2709);
                    	    e=expression();

                    	    state._fsp--;
                    	    if (state.failed) return ;
                    	    if ( state.backtracking==0 ) {
                    	       d.getExpressions().add( (e!=null?input.toString(e.start,e.stop):null) );
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop114;
                        }
                    } while (true);


                    }
                    break;

            }

            c=(Token)match(input,38,FOLLOW_38_in_modifyStatement2728); if (state.failed) return ;
            if ( state.backtracking==0 ) {

                          d.setEnd( ((CommonToken)c).getStopIndex() );
                      
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 84, modifyStatement_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "modifyStatement"


    // $ANTLR start "updateStatement"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:708:1: updateStatement : s= 'update' parExpression ;
    public final void updateStatement() throws RecognitionException {
        int updateStatement_StartIndex = input.index();
        Token s=null;
        JavaParser.parExpression_return parExpression4 = null;



        	    JavaUpdateBlockDescr d = null;
        	
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 85) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:712:2: (s= 'update' parExpression )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:712:4: s= 'update' parExpression
            {
            s=(Token)match(input,88,FOLLOW_88_in_updateStatement2758); if (state.failed) return ;
            pushFollow(FOLLOW_parExpression_in_updateStatement2760);
            parExpression4=parExpression();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {

              	    d = new JavaUpdateBlockDescr( (parExpression4!=null?input.toString(parExpression4.start,parExpression4.stop):null) );
              	    d.setStart( ((CommonToken)s).getStartIndex() );
              	    this.blocks.add( d );
              	    
              	
            }
            if ( state.backtracking==0 ) {

                          d.setEnd( ((CommonToken)$c).getStopIndex() );
                      
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 85, updateStatement_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "updateStatement"


    // $ANTLR start "retractStatement"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:723:1: retractStatement : s= 'retract' parExpression ;
    public final void retractStatement() throws RecognitionException {
        int retractStatement_StartIndex = input.index();
        Token s=null;
        JavaParser.parExpression_return parExpression5 = null;



        	    JavaUpdateBlockDescr d = null;
        	
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 86) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:727:2: (s= 'retract' parExpression )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:727:4: s= 'retract' parExpression
            {
            s=(Token)match(input,89,FOLLOW_89_in_retractStatement2792); if (state.failed) return ;
            pushFollow(FOLLOW_parExpression_in_retractStatement2794);
            parExpression5=parExpression();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {

              	    d = new JavaRetractBlockDescr( (parExpression5!=null?input.toString(parExpression5.start,parExpression5.stop):null) );
              	    d.setStart( ((CommonToken)s).getStartIndex() );
              	    this.blocks.add( d );
              	    
              	
            }
            if ( state.backtracking==0 ) {

                          d.setEnd( ((CommonToken)$c).getStopIndex() );
                      
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 86, retractStatement_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "retractStatement"


    // $ANTLR start "epStatement"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:739:1: epStatement : (s= 'exitPoints' '[' id= StringLiteral c= ']' | s= 'entryPoints' '[' id= StringLiteral c= ']' | s= 'channels' '[' id= StringLiteral c= ']' ) ;
    public final void epStatement() throws RecognitionException {
        int epStatement_StartIndex = input.index();
        Token s=null;
        Token id=null;
        Token c=null;


        	    JavaInterfacePointsDescr d = null;
        	
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 87) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:743:9: ( (s= 'exitPoints' '[' id= StringLiteral c= ']' | s= 'entryPoints' '[' id= StringLiteral c= ']' | s= 'channels' '[' id= StringLiteral c= ']' ) )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:744:9: (s= 'exitPoints' '[' id= StringLiteral c= ']' | s= 'entryPoints' '[' id= StringLiteral c= ']' | s= 'channels' '[' id= StringLiteral c= ']' )
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:744:9: (s= 'exitPoints' '[' id= StringLiteral c= ']' | s= 'entryPoints' '[' id= StringLiteral c= ']' | s= 'channels' '[' id= StringLiteral c= ']' )
            int alt116=3;
            switch ( input.LA(1) ) {
            case 90:
                {
                alt116=1;
                }
                break;
            case 91:
                {
                alt116=2;
                }
                break;
            case 92:
                {
                alt116=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 116, 0, input);

                throw nvae;
            }

            switch (alt116) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:744:11: s= 'exitPoints' '[' id= StringLiteral c= ']'
                    {
                    s=(Token)match(input,90,FOLLOW_90_in_epStatement2848); if (state.failed) return ;
                    match(input,41,FOLLOW_41_in_epStatement2850); if (state.failed) return ;
                    id=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_epStatement2854); if (state.failed) return ;
                    c=(Token)match(input,42,FOLLOW_42_in_epStatement2858); if (state.failed) return ;
                    if ( state.backtracking==0 ) {

                          	    d = new JavaInterfacePointsDescr( (id!=null?id.getText():null) );
                      		    d.setType( JavaBlockDescr.BlockType.EXIT );
                      	    	d.setStart( ((CommonToken)s).getStartIndex() );
                                  d.setEnd( ((CommonToken)c).getStopIndex() );
                      	    	this.blocks.add( d );
                              
                    }

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:752:12: s= 'entryPoints' '[' id= StringLiteral c= ']'
                    {
                    s=(Token)match(input,91,FOLLOW_91_in_epStatement2884); if (state.failed) return ;
                    match(input,41,FOLLOW_41_in_epStatement2886); if (state.failed) return ;
                    id=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_epStatement2890); if (state.failed) return ;
                    c=(Token)match(input,42,FOLLOW_42_in_epStatement2894); if (state.failed) return ;
                    if ( state.backtracking==0 ) {

                      		    d = new JavaInterfacePointsDescr( (id!=null?id.getText():null) );
                      		    d.setType( JavaBlockDescr.BlockType.ENTRY );
                      		    d.setStart( ((CommonToken)s).getStartIndex() );
                                  d.setEnd( ((CommonToken)c).getStopIndex() );
                      		    this.blocks.add( d );
                              
                    }

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:760:12: s= 'channels' '[' id= StringLiteral c= ']'
                    {
                    s=(Token)match(input,92,FOLLOW_92_in_epStatement2920); if (state.failed) return ;
                    match(input,41,FOLLOW_41_in_epStatement2922); if (state.failed) return ;
                    id=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_epStatement2926); if (state.failed) return ;
                    c=(Token)match(input,42,FOLLOW_42_in_epStatement2930); if (state.failed) return ;
                    if ( state.backtracking==0 ) {

                      		    d = new JavaInterfacePointsDescr( (id!=null?id.getText():null) );
                      		    d.setType( JavaBlockDescr.BlockType.CHANNEL );
                      		    d.setStart( ((CommonToken)s).getStartIndex() );
                                  d.setEnd( ((CommonToken)c).getStopIndex() );
                      		    this.blocks.add( d );
                              
                    }

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 87, epStatement_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "epStatement"


    // $ANTLR start "catches"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:771:1: catches : catchClause ( catchClause )* ;
    public final void catches() throws RecognitionException {
        int catches_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 88) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:772:2: ( catchClause ( catchClause )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:772:4: catchClause ( catchClause )*
            {
            pushFollow(FOLLOW_catchClause_in_catches2972);
            catchClause();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:772:16: ( catchClause )*
            loop117:
            do {
                int alt117=2;
                alt117 = dfa117.predict(input);
                switch (alt117) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:772:17: catchClause
            	    {
            	    pushFollow(FOLLOW_catchClause_in_catches2975);
            	    catchClause();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop117;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 88, catches_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "catches"


    // $ANTLR start "catchClause"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:775:1: catchClause : 'catch' '(' formalParameter ')' block ;
    public final void catchClause() throws RecognitionException {
        int catchClause_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 89) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:776:2: ( 'catch' '(' formalParameter ')' block )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:776:4: 'catch' '(' formalParameter ')' block
            {
            match(input,93,FOLLOW_93_in_catchClause2989); if (state.failed) return ;
            match(input,65,FOLLOW_65_in_catchClause2991); if (state.failed) return ;
            pushFollow(FOLLOW_formalParameter_in_catchClause2993);
            formalParameter();

            state._fsp--;
            if (state.failed) return ;
            match(input,66,FOLLOW_66_in_catchClause2995); if (state.failed) return ;
            pushFollow(FOLLOW_block_in_catchClause2997);
            block();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 89, catchClause_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "catchClause"


    // $ANTLR start "formalParameter"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:779:1: formalParameter : ( variableModifier )* type variableDeclaratorId ;
    public final void formalParameter() throws RecognitionException {
        int formalParameter_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 90) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:780:2: ( ( variableModifier )* type variableDeclaratorId )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:780:4: ( variableModifier )* type variableDeclaratorId
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:780:4: ( variableModifier )*
            loop118:
            do {
                int alt118=2;
                int LA118_0 = input.LA(1);

                if ( (LA118_0==49||LA118_0==71) ) {
                    alt118=1;
                }


                switch (alt118) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: variableModifier
            	    {
            	    pushFollow(FOLLOW_variableModifier_in_formalParameter3008);
            	    variableModifier();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop118;
                }
            } while (true);

            pushFollow(FOLLOW_type_in_formalParameter3011);
            type();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_variableDeclaratorId_in_formalParameter3013);
            variableDeclaratorId();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 90, formalParameter_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "formalParameter"


    // $ANTLR start "switchBlockStatementGroups"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:783:1: switchBlockStatementGroups : ( switchBlockStatementGroup )* ;
    public final void switchBlockStatementGroups() throws RecognitionException {
        int switchBlockStatementGroups_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 91) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:784:2: ( ( switchBlockStatementGroup )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:784:4: ( switchBlockStatementGroup )*
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:784:4: ( switchBlockStatementGroup )*
            loop119:
            do {
                int alt119=2;
                int LA119_0 = input.LA(1);

                if ( (LA119_0==72||LA119_0==94) ) {
                    alt119=1;
                }


                switch (alt119) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:784:5: switchBlockStatementGroup
            	    {
            	    pushFollow(FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups3027);
            	    switchBlockStatementGroup();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop119;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 91, switchBlockStatementGroups_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "switchBlockStatementGroups"


    // $ANTLR start "switchBlockStatementGroup"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:787:1: switchBlockStatementGroup : switchLabel ( blockStatement )* ;
    public final void switchBlockStatementGroup() throws RecognitionException {
        int switchBlockStatementGroup_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 92) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:788:2: ( switchLabel ( blockStatement )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:788:4: switchLabel ( blockStatement )*
            {
            pushFollow(FOLLOW_switchLabel_in_switchBlockStatementGroup3041);
            switchLabel();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:788:16: ( blockStatement )*
            loop120:
            do {
                int alt120=2;
                alt120 = dfa120.predict(input);
                switch (alt120) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: blockStatement
            	    {
            	    pushFollow(FOLLOW_blockStatement_in_switchBlockStatementGroup3043);
            	    blockStatement();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop120;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 92, switchBlockStatementGroup_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "switchBlockStatementGroup"


    // $ANTLR start "switchLabel"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:791:1: switchLabel : ( 'case' constantExpression ':' | 'case' enumConstantName ':' | 'default' ':' );
    public final void switchLabel() throws RecognitionException {
        int switchLabel_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 93) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:792:2: ( 'case' constantExpression ':' | 'case' enumConstantName ':' | 'default' ':' )
            int alt121=3;
            alt121 = dfa121.predict(input);
            switch (alt121) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:792:4: 'case' constantExpression ':'
                    {
                    match(input,94,FOLLOW_94_in_switchLabel3056); if (state.failed) return ;
                    pushFollow(FOLLOW_constantExpression_in_switchLabel3058);
                    constantExpression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,74,FOLLOW_74_in_switchLabel3060); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:793:6: 'case' enumConstantName ':'
                    {
                    match(input,94,FOLLOW_94_in_switchLabel3067); if (state.failed) return ;
                    pushFollow(FOLLOW_enumConstantName_in_switchLabel3069);
                    enumConstantName();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,74,FOLLOW_74_in_switchLabel3071); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:794:6: 'default' ':'
                    {
                    match(input,72,FOLLOW_72_in_switchLabel3078); if (state.failed) return ;
                    match(input,74,FOLLOW_74_in_switchLabel3080); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 93, switchLabel_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "switchLabel"


    // $ANTLR start "moreStatementExpressions"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:797:1: moreStatementExpressions : ( ',' statementExpression )* ;
    public final void moreStatementExpressions() throws RecognitionException {
        int moreStatementExpressions_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 94) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:798:2: ( ( ',' statementExpression )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:798:4: ( ',' statementExpression )*
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:798:4: ( ',' statementExpression )*
            loop122:
            do {
                int alt122=2;
                int LA122_0 = input.LA(1);

                if ( (LA122_0==34) ) {
                    alt122=1;
                }


                switch (alt122) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:798:5: ',' statementExpression
            	    {
            	    match(input,34,FOLLOW_34_in_moreStatementExpressions3093); if (state.failed) return ;
            	    pushFollow(FOLLOW_statementExpression_in_moreStatementExpressions3095);
            	    statementExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop122;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 94, moreStatementExpressions_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "moreStatementExpressions"


    // $ANTLR start "forControl"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:801:1: forControl options {k=3; } : ( forVarControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );
    public final void forControl() throws RecognitionException {
        int forControl_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 95) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:803:2: ( forVarControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? )
            int alt126=2;
            alt126 = dfa126.predict(input);
            switch (alt126) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:803:4: forVarControl
                    {
                    pushFollow(FOLLOW_forVarControl_in_forControl3116);
                    forVarControl();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:804:4: ( forInit )? ';' ( expression )? ';' ( forUpdate )?
                    {
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:804:4: ( forInit )?
                    int alt123=2;
                    alt123 = dfa123.predict(input);
                    switch (alt123) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: forInit
                            {
                            pushFollow(FOLLOW_forInit_in_forControl3121);
                            forInit();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,25,FOLLOW_25_in_forControl3124); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:804:17: ( expression )?
                    int alt124=2;
                    alt124 = dfa124.predict(input);
                    switch (alt124) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: expression
                            {
                            pushFollow(FOLLOW_expression_in_forControl3126);
                            expression();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,25,FOLLOW_25_in_forControl3129); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:804:33: ( forUpdate )?
                    int alt125=2;
                    alt125 = dfa125.predict(input);
                    switch (alt125) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: forUpdate
                            {
                            pushFollow(FOLLOW_forUpdate_in_forControl3131);
                            forUpdate();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 95, forControl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "forControl"


    // $ANTLR start "forInit"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:807:1: forInit : ( ( variableModifier )* type variableDeclarators | expressionList );
    public final void forInit() throws RecognitionException {
        int forInit_StartIndex = input.index();

                    this.localVariableLevel++;
                
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 96) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:814:2: ( ( variableModifier )* type variableDeclarators | expressionList )
            int alt128=2;
            alt128 = dfa128.predict(input);
            switch (alt128) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:814:4: ( variableModifier )* type variableDeclarators
                    {
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:814:4: ( variableModifier )*
                    loop127:
                    do {
                        int alt127=2;
                        int LA127_0 = input.LA(1);

                        if ( (LA127_0==49||LA127_0==71) ) {
                            alt127=1;
                        }


                        switch (alt127) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: variableModifier
                    	    {
                    	    pushFollow(FOLLOW_variableModifier_in_forInit3169);
                    	    variableModifier();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop127;
                        }
                    } while (true);

                    pushFollow(FOLLOW_type_in_forInit3172);
                    type();

                    state._fsp--;
                    if (state.failed) return ;
                    pushFollow(FOLLOW_variableDeclarators_in_forInit3174);
                    variableDeclarators();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:815:4: expressionList
                    {
                    pushFollow(FOLLOW_expressionList_in_forInit3179);
                    expressionList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
            if ( state.backtracking==0 ) {

                          this.localVariableLevel--;
                      
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 96, forInit_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "forInit"


    // $ANTLR start "forVarControl"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:818:1: forVarControl : ( variableModifier )* type Identifier ':' expression ;
    public final void forVarControl() throws RecognitionException {
        int forVarControl_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 97) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:819:2: ( ( variableModifier )* type Identifier ':' expression )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:819:4: ( variableModifier )* type Identifier ':' expression
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:819:4: ( variableModifier )*
            loop129:
            do {
                int alt129=2;
                int LA129_0 = input.LA(1);

                if ( (LA129_0==49||LA129_0==71) ) {
                    alt129=1;
                }


                switch (alt129) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: variableModifier
            	    {
            	    pushFollow(FOLLOW_variableModifier_in_forVarControl3191);
            	    variableModifier();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop129;
                }
            } while (true);

            pushFollow(FOLLOW_type_in_forVarControl3194);
            type();

            state._fsp--;
            if (state.failed) return ;
            match(input,Identifier,FOLLOW_Identifier_in_forVarControl3196); if (state.failed) return ;
            match(input,74,FOLLOW_74_in_forVarControl3198); if (state.failed) return ;
            pushFollow(FOLLOW_expression_in_forVarControl3200);
            expression();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 97, forVarControl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "forVarControl"


    // $ANTLR start "forUpdate"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:822:1: forUpdate : expressionList ;
    public final void forUpdate() throws RecognitionException {
        int forUpdate_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 98) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:823:2: ( expressionList )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:823:4: expressionList
            {
            pushFollow(FOLLOW_expressionList_in_forUpdate3211);
            expressionList();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 98, forUpdate_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "forUpdate"

    public static class parExpression_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "parExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:828:1: parExpression : '(' expression ')' ;
    public final JavaParser.parExpression_return parExpression() throws RecognitionException {
        JavaParser.parExpression_return retval = new JavaParser.parExpression_return();
        retval.start = input.LT(1);
        int parExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 99) ) { return retval; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:829:2: ( '(' expression ')' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:829:4: '(' expression ')'
            {
            match(input,65,FOLLOW_65_in_parExpression3224); if (state.failed) return retval;
            pushFollow(FOLLOW_expression_in_parExpression3226);
            expression();

            state._fsp--;
            if (state.failed) return retval;
            match(input,66,FOLLOW_66_in_parExpression3228); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 99, parExpression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "parExpression"


    // $ANTLR start "expressionList"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:832:1: expressionList : expression ( ',' expression )* ;
    public final void expressionList() throws RecognitionException {
        int expressionList_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 100) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:833:5: ( expression ( ',' expression )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:833:9: expression ( ',' expression )*
            {
            pushFollow(FOLLOW_expression_in_expressionList3245);
            expression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:833:20: ( ',' expression )*
            loop130:
            do {
                int alt130=2;
                int LA130_0 = input.LA(1);

                if ( (LA130_0==34) ) {
                    alt130=1;
                }


                switch (alt130) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:833:21: ',' expression
            	    {
            	    match(input,34,FOLLOW_34_in_expressionList3248); if (state.failed) return ;
            	    pushFollow(FOLLOW_expression_in_expressionList3250);
            	    expression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop130;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 100, expressionList_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "expressionList"


    // $ANTLR start "statementExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:836:1: statementExpression : expression ;
    public final void statementExpression() throws RecognitionException {
        int statementExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 101) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:837:2: ( expression )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:837:4: expression
            {
            pushFollow(FOLLOW_expression_in_statementExpression3266);
            expression();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 101, statementExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "statementExpression"


    // $ANTLR start "constantExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:840:1: constantExpression : expression ;
    public final void constantExpression() throws RecognitionException {
        int constantExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 102) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:841:2: ( expression )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:841:4: expression
            {
            pushFollow(FOLLOW_expression_in_constantExpression3278);
            expression();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 102, constantExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "constantExpression"

    public static class expression_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "expression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:844:1: expression : conditionalExpression ( assignmentOperator expression )? ;
    public final JavaParser.expression_return expression() throws RecognitionException {
        JavaParser.expression_return retval = new JavaParser.expression_return();
        retval.start = input.LT(1);
        int expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 103) ) { return retval; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:845:2: ( conditionalExpression ( assignmentOperator expression )? )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:845:4: conditionalExpression ( assignmentOperator expression )?
            {
            pushFollow(FOLLOW_conditionalExpression_in_expression3290);
            conditionalExpression();

            state._fsp--;
            if (state.failed) return retval;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:845:26: ( assignmentOperator expression )?
            int alt131=2;
            alt131 = dfa131.predict(input);
            switch (alt131) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:845:27: assignmentOperator expression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_expression3293);
                    assignmentOperator();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_expression_in_expression3295);
                    expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 103, expression_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "expression"


    // $ANTLR start "assignmentOperator"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:848:1: assignmentOperator : ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | '<' '<' '=' | '>' '>' '=' | '>' '>' '>' '=' );
    public final void assignmentOperator() throws RecognitionException {
        int assignmentOperator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 104) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:849:2: ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | '<' '<' '=' | '>' '>' '=' | '>' '>' '>' '=' )
            int alt132=12;
            alt132 = dfa132.predict(input);
            switch (alt132) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:849:4: '='
                    {
                    match(input,44,FOLLOW_44_in_assignmentOperator3309); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:850:9: '+='
                    {
                    match(input,95,FOLLOW_95_in_assignmentOperator3319); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:851:9: '-='
                    {
                    match(input,96,FOLLOW_96_in_assignmentOperator3329); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:852:9: '*='
                    {
                    match(input,97,FOLLOW_97_in_assignmentOperator3339); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:853:9: '/='
                    {
                    match(input,98,FOLLOW_98_in_assignmentOperator3349); if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:854:9: '&='
                    {
                    match(input,99,FOLLOW_99_in_assignmentOperator3359); if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:855:9: '|='
                    {
                    match(input,100,FOLLOW_100_in_assignmentOperator3369); if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:856:9: '^='
                    {
                    match(input,101,FOLLOW_101_in_assignmentOperator3379); if (state.failed) return ;

                    }
                    break;
                case 9 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:857:9: '%='
                    {
                    match(input,102,FOLLOW_102_in_assignmentOperator3389); if (state.failed) return ;

                    }
                    break;
                case 10 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:858:9: '<' '<' '='
                    {
                    match(input,33,FOLLOW_33_in_assignmentOperator3399); if (state.failed) return ;
                    match(input,33,FOLLOW_33_in_assignmentOperator3401); if (state.failed) return ;
                    match(input,44,FOLLOW_44_in_assignmentOperator3403); if (state.failed) return ;

                    }
                    break;
                case 11 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:859:9: '>' '>' '='
                    {
                    match(input,35,FOLLOW_35_in_assignmentOperator3413); if (state.failed) return ;
                    match(input,35,FOLLOW_35_in_assignmentOperator3415); if (state.failed) return ;
                    match(input,44,FOLLOW_44_in_assignmentOperator3417); if (state.failed) return ;

                    }
                    break;
                case 12 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:860:9: '>' '>' '>' '='
                    {
                    match(input,35,FOLLOW_35_in_assignmentOperator3427); if (state.failed) return ;
                    match(input,35,FOLLOW_35_in_assignmentOperator3429); if (state.failed) return ;
                    match(input,35,FOLLOW_35_in_assignmentOperator3431); if (state.failed) return ;
                    match(input,44,FOLLOW_44_in_assignmentOperator3433); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 104, assignmentOperator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "assignmentOperator"


    // $ANTLR start "conditionalExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:863:1: conditionalExpression : conditionalOrExpression ( '?' expression ':' expression )? ;
    public final void conditionalExpression() throws RecognitionException {
        int conditionalExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 105) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:864:5: ( conditionalOrExpression ( '?' expression ':' expression )? )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:864:9: conditionalOrExpression ( '?' expression ':' expression )?
            {
            pushFollow(FOLLOW_conditionalOrExpression_in_conditionalExpression3449);
            conditionalOrExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:864:33: ( '?' expression ':' expression )?
            int alt133=2;
            alt133 = dfa133.predict(input);
            switch (alt133) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:864:35: '?' expression ':' expression
                    {
                    match(input,63,FOLLOW_63_in_conditionalExpression3453); if (state.failed) return ;
                    pushFollow(FOLLOW_expression_in_conditionalExpression3455);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,74,FOLLOW_74_in_conditionalExpression3457); if (state.failed) return ;
                    pushFollow(FOLLOW_expression_in_conditionalExpression3459);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 105, conditionalExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "conditionalExpression"


    // $ANTLR start "conditionalOrExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:867:1: conditionalOrExpression : conditionalAndExpression ( '||' conditionalAndExpression )* ;
    public final void conditionalOrExpression() throws RecognitionException {
        int conditionalOrExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 106) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:868:5: ( conditionalAndExpression ( '||' conditionalAndExpression )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:868:9: conditionalAndExpression ( '||' conditionalAndExpression )*
            {
            pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression3478);
            conditionalAndExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:868:34: ( '||' conditionalAndExpression )*
            loop134:
            do {
                int alt134=2;
                alt134 = dfa134.predict(input);
                switch (alt134) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:868:36: '||' conditionalAndExpression
            	    {
            	    match(input,103,FOLLOW_103_in_conditionalOrExpression3482); if (state.failed) return ;
            	    pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression3484);
            	    conditionalAndExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop134;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 106, conditionalOrExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "conditionalOrExpression"


    // $ANTLR start "conditionalAndExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:871:1: conditionalAndExpression : inclusiveOrExpression ( '&&' inclusiveOrExpression )* ;
    public final void conditionalAndExpression() throws RecognitionException {
        int conditionalAndExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 107) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:872:5: ( inclusiveOrExpression ( '&&' inclusiveOrExpression )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:872:9: inclusiveOrExpression ( '&&' inclusiveOrExpression )*
            {
            pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression3503);
            inclusiveOrExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:872:31: ( '&&' inclusiveOrExpression )*
            loop135:
            do {
                int alt135=2;
                alt135 = dfa135.predict(input);
                switch (alt135) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:872:33: '&&' inclusiveOrExpression
            	    {
            	    match(input,104,FOLLOW_104_in_conditionalAndExpression3507); if (state.failed) return ;
            	    pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression3509);
            	    inclusiveOrExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop135;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 107, conditionalAndExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "conditionalAndExpression"


    // $ANTLR start "inclusiveOrExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:875:1: inclusiveOrExpression : exclusiveOrExpression ( '|' exclusiveOrExpression )* ;
    public final void inclusiveOrExpression() throws RecognitionException {
        int inclusiveOrExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 108) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:876:5: ( exclusiveOrExpression ( '|' exclusiveOrExpression )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:876:9: exclusiveOrExpression ( '|' exclusiveOrExpression )*
            {
            pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression3528);
            exclusiveOrExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:876:31: ( '|' exclusiveOrExpression )*
            loop136:
            do {
                int alt136=2;
                alt136 = dfa136.predict(input);
                switch (alt136) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:876:33: '|' exclusiveOrExpression
            	    {
            	    match(input,105,FOLLOW_105_in_inclusiveOrExpression3532); if (state.failed) return ;
            	    pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression3534);
            	    exclusiveOrExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop136;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 108, inclusiveOrExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "inclusiveOrExpression"


    // $ANTLR start "exclusiveOrExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:879:1: exclusiveOrExpression : andExpression ( '^' andExpression )* ;
    public final void exclusiveOrExpression() throws RecognitionException {
        int exclusiveOrExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 109) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:880:5: ( andExpression ( '^' andExpression )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:880:9: andExpression ( '^' andExpression )*
            {
            pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression3553);
            andExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:880:23: ( '^' andExpression )*
            loop137:
            do {
                int alt137=2;
                alt137 = dfa137.predict(input);
                switch (alt137) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:880:25: '^' andExpression
            	    {
            	    match(input,106,FOLLOW_106_in_exclusiveOrExpression3557); if (state.failed) return ;
            	    pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression3559);
            	    andExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop137;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 109, exclusiveOrExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "exclusiveOrExpression"


    // $ANTLR start "andExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:883:1: andExpression : equalityExpression ( '&' equalityExpression )* ;
    public final void andExpression() throws RecognitionException {
        int andExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 110) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:884:5: ( equalityExpression ( '&' equalityExpression )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:884:9: equalityExpression ( '&' equalityExpression )*
            {
            pushFollow(FOLLOW_equalityExpression_in_andExpression3578);
            equalityExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:884:28: ( '&' equalityExpression )*
            loop138:
            do {
                int alt138=2;
                alt138 = dfa138.predict(input);
                switch (alt138) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:884:30: '&' equalityExpression
            	    {
            	    match(input,36,FOLLOW_36_in_andExpression3582); if (state.failed) return ;
            	    pushFollow(FOLLOW_equalityExpression_in_andExpression3584);
            	    equalityExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop138;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 110, andExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "andExpression"


    // $ANTLR start "equalityExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:887:1: equalityExpression : instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* ;
    public final void equalityExpression() throws RecognitionException {
        int equalityExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 111) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:888:5: ( instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:888:9: instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression3603);
            instanceOfExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:888:30: ( ( '==' | '!=' ) instanceOfExpression )*
            loop139:
            do {
                int alt139=2;
                alt139 = dfa139.predict(input);
                switch (alt139) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:888:32: ( '==' | '!=' ) instanceOfExpression
            	    {
            	    if ( (input.LA(1)>=107 && input.LA(1)<=108) ) {
            	        input.consume();
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression3615);
            	    instanceOfExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop139;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 111, equalityExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "equalityExpression"


    // $ANTLR start "instanceOfExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:891:1: instanceOfExpression : relationalExpression ( 'instanceof' type )? ;
    public final void instanceOfExpression() throws RecognitionException {
        int instanceOfExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 112) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:892:5: ( relationalExpression ( 'instanceof' type )? )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:892:9: relationalExpression ( 'instanceof' type )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression3634);
            relationalExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:892:30: ( 'instanceof' type )?
            int alt140=2;
            alt140 = dfa140.predict(input);
            switch (alt140) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:892:31: 'instanceof' type
                    {
                    match(input,109,FOLLOW_109_in_instanceOfExpression3637); if (state.failed) return ;
                    pushFollow(FOLLOW_type_in_instanceOfExpression3639);
                    type();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 112, instanceOfExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "instanceOfExpression"


    // $ANTLR start "relationalExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:895:1: relationalExpression : shiftExpression ( relationalOp shiftExpression )* ;
    public final void relationalExpression() throws RecognitionException {
        int relationalExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 113) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:896:5: ( shiftExpression ( relationalOp shiftExpression )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:896:9: shiftExpression ( relationalOp shiftExpression )*
            {
            pushFollow(FOLLOW_shiftExpression_in_relationalExpression3657);
            shiftExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:896:25: ( relationalOp shiftExpression )*
            loop141:
            do {
                int alt141=2;
                alt141 = dfa141.predict(input);
                switch (alt141) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:896:27: relationalOp shiftExpression
            	    {
            	    pushFollow(FOLLOW_relationalOp_in_relationalExpression3661);
            	    relationalOp();

            	    state._fsp--;
            	    if (state.failed) return ;
            	    pushFollow(FOLLOW_shiftExpression_in_relationalExpression3663);
            	    shiftExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop141;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 113, relationalExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "relationalExpression"


    // $ANTLR start "relationalOp"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:899:1: relationalOp : ( '<' '=' | '>' '=' | '<' | '>' ) ;
    public final void relationalOp() throws RecognitionException {
        int relationalOp_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 114) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:900:2: ( ( '<' '=' | '>' '=' | '<' | '>' ) )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:900:4: ( '<' '=' | '>' '=' | '<' | '>' )
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:900:4: ( '<' '=' | '>' '=' | '<' | '>' )
            int alt142=4;
            alt142 = dfa142.predict(input);
            switch (alt142) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:900:5: '<' '='
                    {
                    match(input,33,FOLLOW_33_in_relationalOp3679); if (state.failed) return ;
                    match(input,44,FOLLOW_44_in_relationalOp3681); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:900:15: '>' '='
                    {
                    match(input,35,FOLLOW_35_in_relationalOp3685); if (state.failed) return ;
                    match(input,44,FOLLOW_44_in_relationalOp3687); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:900:25: '<'
                    {
                    match(input,33,FOLLOW_33_in_relationalOp3691); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:900:31: '>'
                    {
                    match(input,35,FOLLOW_35_in_relationalOp3695); if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 114, relationalOp_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "relationalOp"


    // $ANTLR start "shiftExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:903:1: shiftExpression : additiveExpression ( shiftOp additiveExpression )* ;
    public final void shiftExpression() throws RecognitionException {
        int shiftExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 115) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:904:5: ( additiveExpression ( shiftOp additiveExpression )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:904:9: additiveExpression ( shiftOp additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_shiftExpression3712);
            additiveExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:904:28: ( shiftOp additiveExpression )*
            loop143:
            do {
                int alt143=2;
                alt143 = dfa143.predict(input);
                switch (alt143) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:904:30: shiftOp additiveExpression
            	    {
            	    pushFollow(FOLLOW_shiftOp_in_shiftExpression3716);
            	    shiftOp();

            	    state._fsp--;
            	    if (state.failed) return ;
            	    pushFollow(FOLLOW_additiveExpression_in_shiftExpression3718);
            	    additiveExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop143;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 115, shiftExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "shiftExpression"


    // $ANTLR start "shiftOp"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:908:1: shiftOp : ( '<' '<' | '>' '>' '>' | '>' '>' ) ;
    public final void shiftOp() throws RecognitionException {
        int shiftOp_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 116) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:909:2: ( ( '<' '<' | '>' '>' '>' | '>' '>' ) )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:909:4: ( '<' '<' | '>' '>' '>' | '>' '>' )
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:909:4: ( '<' '<' | '>' '>' '>' | '>' '>' )
            int alt144=3;
            int LA144_0 = input.LA(1);

            if ( (LA144_0==33) ) {
                alt144=1;
            }
            else if ( (LA144_0==35) ) {
                int LA144_2 = input.LA(2);

                if ( (LA144_2==35) ) {
                    int LA144_3 = input.LA(3);

                    if ( (synpred216_Java()) ) {
                        alt144=2;
                    }
                    else if ( (true) ) {
                        alt144=3;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 144, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 144, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 144, 0, input);

                throw nvae;
            }
            switch (alt144) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:909:5: '<' '<'
                    {
                    match(input,33,FOLLOW_33_in_shiftOp3742); if (state.failed) return ;
                    match(input,33,FOLLOW_33_in_shiftOp3744); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:909:15: '>' '>' '>'
                    {
                    match(input,35,FOLLOW_35_in_shiftOp3748); if (state.failed) return ;
                    match(input,35,FOLLOW_35_in_shiftOp3750); if (state.failed) return ;
                    match(input,35,FOLLOW_35_in_shiftOp3752); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:909:29: '>' '>'
                    {
                    match(input,35,FOLLOW_35_in_shiftOp3756); if (state.failed) return ;
                    match(input,35,FOLLOW_35_in_shiftOp3758); if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 116, shiftOp_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "shiftOp"


    // $ANTLR start "additiveExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:913:1: additiveExpression : multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* ;
    public final void additiveExpression() throws RecognitionException {
        int additiveExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 117) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:914:5: ( multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:914:9: multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression3776);
            multiplicativeExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:914:34: ( ( '+' | '-' ) multiplicativeExpression )*
            loop145:
            do {
                int alt145=2;
                alt145 = dfa145.predict(input);
                switch (alt145) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:914:36: ( '+' | '-' ) multiplicativeExpression
            	    {
            	    if ( (input.LA(1)>=110 && input.LA(1)<=111) ) {
            	        input.consume();
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression3788);
            	    multiplicativeExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop145;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 117, additiveExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "additiveExpression"


    // $ANTLR start "multiplicativeExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:917:1: multiplicativeExpression : unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* ;
    public final void multiplicativeExpression() throws RecognitionException {
        int multiplicativeExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 118) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:918:5: ( unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:918:9: unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression3807);
            unaryExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:918:25: ( ( '*' | '/' | '%' ) unaryExpression )*
            loop146:
            do {
                int alt146=2;
                alt146 = dfa146.predict(input);
                switch (alt146) {
            	case 1 :
            	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:918:27: ( '*' | '/' | '%' ) unaryExpression
            	    {
            	    if ( input.LA(1)==29||(input.LA(1)>=112 && input.LA(1)<=113) ) {
            	        input.consume();
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression3825);
            	    unaryExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop146;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 118, multiplicativeExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "multiplicativeExpression"


    // $ANTLR start "unaryExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:921:1: unaryExpression : ( '+' unaryExpression | '-' unaryExpression | '++' primary | '--' primary | unaryExpressionNotPlusMinus );
    public final void unaryExpression() throws RecognitionException {
        int unaryExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 119) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:922:5: ( '+' unaryExpression | '-' unaryExpression | '++' primary | '--' primary | unaryExpressionNotPlusMinus )
            int alt147=5;
            alt147 = dfa147.predict(input);
            switch (alt147) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:922:9: '+' unaryExpression
                    {
                    match(input,110,FOLLOW_110_in_unaryExpression3845); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression3847);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:923:7: '-' unaryExpression
                    {
                    match(input,111,FOLLOW_111_in_unaryExpression3855); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression3857);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:924:9: '++' primary
                    {
                    match(input,114,FOLLOW_114_in_unaryExpression3867); if (state.failed) return ;
                    pushFollow(FOLLOW_primary_in_unaryExpression3869);
                    primary();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:925:9: '--' primary
                    {
                    match(input,115,FOLLOW_115_in_unaryExpression3879); if (state.failed) return ;
                    pushFollow(FOLLOW_primary_in_unaryExpression3881);
                    primary();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:926:9: unaryExpressionNotPlusMinus
                    {
                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression3891);
                    unaryExpressionNotPlusMinus();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 119, unaryExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "unaryExpression"


    // $ANTLR start "unaryExpressionNotPlusMinus"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:929:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );
    public final void unaryExpressionNotPlusMinus() throws RecognitionException {
        int unaryExpressionNotPlusMinus_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 120) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:930:5: ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? )
            int alt150=4;
            alt150 = dfa150.predict(input);
            switch (alt150) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:930:9: '~' unaryExpression
                    {
                    match(input,116,FOLLOW_116_in_unaryExpressionNotPlusMinus3910); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus3912);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:931:8: '!' unaryExpression
                    {
                    match(input,117,FOLLOW_117_in_unaryExpressionNotPlusMinus3921); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus3923);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:932:9: castExpression
                    {
                    pushFollow(FOLLOW_castExpression_in_unaryExpressionNotPlusMinus3933);
                    castExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:933:9: primary ( selector )* ( '++' | '--' )?
                    {
                    pushFollow(FOLLOW_primary_in_unaryExpressionNotPlusMinus3943);
                    primary();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:933:17: ( selector )*
                    loop148:
                    do {
                        int alt148=2;
                        alt148 = dfa148.predict(input);
                        switch (alt148) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: selector
                    	    {
                    	    pushFollow(FOLLOW_selector_in_unaryExpressionNotPlusMinus3945);
                    	    selector();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop148;
                        }
                    } while (true);

                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:933:27: ( '++' | '--' )?
                    int alt149=2;
                    alt149 = dfa149.predict(input);
                    switch (alt149) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:
                            {
                            if ( (input.LA(1)>=114 && input.LA(1)<=115) ) {
                                input.consume();
                                state.errorRecovery=false;state.failed=false;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return ;}
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 120, unaryExpressionNotPlusMinus_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "unaryExpressionNotPlusMinus"


    // $ANTLR start "castExpression"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:936:1: castExpression : ( '(' primitiveType ')' unaryExpression | '(' ( type | expression ) ')' unaryExpressionNotPlusMinus );
    public final void castExpression() throws RecognitionException {
        int castExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 121) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:937:5: ( '(' primitiveType ')' unaryExpression | '(' ( type | expression ) ')' unaryExpressionNotPlusMinus )
            int alt152=2;
            alt152 = dfa152.predict(input);
            switch (alt152) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:937:8: '(' primitiveType ')' unaryExpression
                    {
                    match(input,65,FOLLOW_65_in_castExpression3971); if (state.failed) return ;
                    pushFollow(FOLLOW_primitiveType_in_castExpression3973);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,66,FOLLOW_66_in_castExpression3975); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_castExpression3977);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:938:8: '(' ( type | expression ) ')' unaryExpressionNotPlusMinus
                    {
                    match(input,65,FOLLOW_65_in_castExpression3986); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:938:12: ( type | expression )
                    int alt151=2;
                    alt151 = dfa151.predict(input);
                    switch (alt151) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:938:13: type
                            {
                            pushFollow(FOLLOW_type_in_castExpression3989);
                            type();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;
                        case 2 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:938:20: expression
                            {
                            pushFollow(FOLLOW_expression_in_castExpression3993);
                            expression();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,66,FOLLOW_66_in_castExpression3996); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_castExpression3998);
                    unaryExpressionNotPlusMinus();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 121, castExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "castExpression"


    // $ANTLR start "primary"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:941:1: primary : ( parExpression | nonWildcardTypeArguments ( explicitGenericInvocationSuffix | 'this' arguments ) | 'this' ( '.' Identifier )* ( identifierSuffix )? | 'super' superSuffix | epStatement ( '.' Identifier )* ( identifierSuffix )? | literal | 'new' creator | i= Identifier ( '.' Identifier )* ( identifierSuffix )? | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' );
    public final void primary() throws RecognitionException {
        int primary_StartIndex = input.index();
        Token i=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 122) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:942:5: ( parExpression | nonWildcardTypeArguments ( explicitGenericInvocationSuffix | 'this' arguments ) | 'this' ( '.' Identifier )* ( identifierSuffix )? | 'super' superSuffix | epStatement ( '.' Identifier )* ( identifierSuffix )? | literal | 'new' creator | i= Identifier ( '.' Identifier )* ( identifierSuffix )? | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' )
            int alt161=10;
            alt161 = dfa161.predict(input);
            switch (alt161) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:942:7: parExpression
                    {
                    pushFollow(FOLLOW_parExpression_in_primary4015);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:943:9: nonWildcardTypeArguments ( explicitGenericInvocationSuffix | 'this' arguments )
                    {
                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_primary4025);
                    nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:944:9: ( explicitGenericInvocationSuffix | 'this' arguments )
                    int alt153=2;
                    int LA153_0 = input.LA(1);

                    if ( (LA153_0==Identifier||LA153_0==64) ) {
                        alt153=1;
                    }
                    else if ( (LA153_0==118) ) {
                        alt153=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 153, 0, input);

                        throw nvae;
                    }
                    switch (alt153) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:944:10: explicitGenericInvocationSuffix
                            {
                            pushFollow(FOLLOW_explicitGenericInvocationSuffix_in_primary4036);
                            explicitGenericInvocationSuffix();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;
                        case 2 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:944:44: 'this' arguments
                            {
                            match(input,118,FOLLOW_118_in_primary4040); if (state.failed) return ;
                            pushFollow(FOLLOW_arguments_in_primary4042);
                            arguments();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:945:9: 'this' ( '.' Identifier )* ( identifierSuffix )?
                    {
                    match(input,118,FOLLOW_118_in_primary4053); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:945:16: ( '.' Identifier )*
                    loop154:
                    do {
                        int alt154=2;
                        alt154 = dfa154.predict(input);
                        switch (alt154) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:945:17: '.' Identifier
                    	    {
                    	    match(input,28,FOLLOW_28_in_primary4056); if (state.failed) return ;
                    	    match(input,Identifier,FOLLOW_Identifier_in_primary4058); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop154;
                        }
                    } while (true);

                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:945:34: ( identifierSuffix )?
                    int alt155=2;
                    alt155 = dfa155.predict(input);
                    switch (alt155) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:945:35: identifierSuffix
                            {
                            pushFollow(FOLLOW_identifierSuffix_in_primary4063);
                            identifierSuffix();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:946:9: 'super' superSuffix
                    {
                    match(input,64,FOLLOW_64_in_primary4075); if (state.failed) return ;
                    pushFollow(FOLLOW_superSuffix_in_primary4077);
                    superSuffix();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:947:9: epStatement ( '.' Identifier )* ( identifierSuffix )?
                    {
                    pushFollow(FOLLOW_epStatement_in_primary4087);
                    epStatement();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:947:21: ( '.' Identifier )*
                    loop156:
                    do {
                        int alt156=2;
                        alt156 = dfa156.predict(input);
                        switch (alt156) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:947:22: '.' Identifier
                    	    {
                    	    match(input,28,FOLLOW_28_in_primary4090); if (state.failed) return ;
                    	    match(input,Identifier,FOLLOW_Identifier_in_primary4092); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop156;
                        }
                    } while (true);

                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:947:39: ( identifierSuffix )?
                    int alt157=2;
                    alt157 = dfa157.predict(input);
                    switch (alt157) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:947:40: identifierSuffix
                            {
                            pushFollow(FOLLOW_identifierSuffix_in_primary4097);
                            identifierSuffix();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 6 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:948:9: literal
                    {
                    pushFollow(FOLLOW_literal_in_primary4109);
                    literal();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:949:9: 'new' creator
                    {
                    match(input,119,FOLLOW_119_in_primary4119); if (state.failed) return ;
                    pushFollow(FOLLOW_creator_in_primary4121);
                    creator();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:950:9: i= Identifier ( '.' Identifier )* ( identifierSuffix )?
                    {
                    i=(Token)match(input,Identifier,FOLLOW_Identifier_in_primary4133); if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                       if( ! "(".equals( input.LT(1) == null ? "" : input.LT(1).getText() ) ) identifiers.add( (i!=null?i.getText():null) );
                    }
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:950:126: ( '.' Identifier )*
                    loop158:
                    do {
                        int alt158=2;
                        alt158 = dfa158.predict(input);
                        switch (alt158) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:950:127: '.' Identifier
                    	    {
                    	    match(input,28,FOLLOW_28_in_primary4138); if (state.failed) return ;
                    	    match(input,Identifier,FOLLOW_Identifier_in_primary4140); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop158;
                        }
                    } while (true);

                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:950:144: ( identifierSuffix )?
                    int alt159=2;
                    alt159 = dfa159.predict(input);
                    switch (alt159) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:950:145: identifierSuffix
                            {
                            pushFollow(FOLLOW_identifierSuffix_in_primary4145);
                            identifierSuffix();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 9 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:951:9: primitiveType ( '[' ']' )* '.' 'class'
                    {
                    pushFollow(FOLLOW_primitiveType_in_primary4157);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:951:23: ( '[' ']' )*
                    loop160:
                    do {
                        int alt160=2;
                        int LA160_0 = input.LA(1);

                        if ( (LA160_0==41) ) {
                            alt160=1;
                        }


                        switch (alt160) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:951:24: '[' ']'
                    	    {
                    	    match(input,41,FOLLOW_41_in_primary4160); if (state.failed) return ;
                    	    match(input,42,FOLLOW_42_in_primary4162); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop160;
                        }
                    } while (true);

                    match(input,28,FOLLOW_28_in_primary4166); if (state.failed) return ;
                    match(input,30,FOLLOW_30_in_primary4168); if (state.failed) return ;

                    }
                    break;
                case 10 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:952:9: 'void' '.' 'class'
                    {
                    match(input,40,FOLLOW_40_in_primary4178); if (state.failed) return ;
                    match(input,28,FOLLOW_28_in_primary4180); if (state.failed) return ;
                    match(input,30,FOLLOW_30_in_primary4182); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 122, primary_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "primary"


    // $ANTLR start "identifierSuffix"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:955:1: identifierSuffix : ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' explicitGenericInvocation | '.' 'this' | '.' 'super' arguments | '.' 'new' ( nonWildcardTypeArguments )? innerCreator );
    public final void identifierSuffix() throws RecognitionException {
        int identifierSuffix_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 123) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:956:2: ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' explicitGenericInvocation | '.' 'this' | '.' 'super' arguments | '.' 'new' ( nonWildcardTypeArguments )? innerCreator )
            int alt165=8;
            alt165 = dfa165.predict(input);
            switch (alt165) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:956:4: ( '[' ']' )+ '.' 'class'
                    {
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:956:4: ( '[' ']' )+
                    int cnt162=0;
                    loop162:
                    do {
                        int alt162=2;
                        int LA162_0 = input.LA(1);

                        if ( (LA162_0==41) ) {
                            alt162=1;
                        }


                        switch (alt162) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:956:5: '[' ']'
                    	    {
                    	    match(input,41,FOLLOW_41_in_identifierSuffix4194); if (state.failed) return ;
                    	    match(input,42,FOLLOW_42_in_identifierSuffix4196); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt162 >= 1 ) break loop162;
                    	    if (state.backtracking>0) {state.failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(162, input);
                                throw eee;
                        }
                        cnt162++;
                    } while (true);

                    match(input,28,FOLLOW_28_in_identifierSuffix4200); if (state.failed) return ;
                    match(input,30,FOLLOW_30_in_identifierSuffix4202); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:957:4: ( '[' expression ']' )+
                    {
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:957:4: ( '[' expression ']' )+
                    int cnt163=0;
                    loop163:
                    do {
                        int alt163=2;
                        alt163 = dfa163.predict(input);
                        switch (alt163) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:957:5: '[' expression ']'
                    	    {
                    	    match(input,41,FOLLOW_41_in_identifierSuffix4208); if (state.failed) return ;
                    	    pushFollow(FOLLOW_expression_in_identifierSuffix4210);
                    	    expression();

                    	    state._fsp--;
                    	    if (state.failed) return ;
                    	    match(input,42,FOLLOW_42_in_identifierSuffix4212); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt163 >= 1 ) break loop163;
                    	    if (state.backtracking>0) {state.failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(163, input);
                                throw eee;
                        }
                        cnt163++;
                    } while (true);


                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:958:9: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_identifierSuffix4225);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:959:9: '.' 'class'
                    {
                    match(input,28,FOLLOW_28_in_identifierSuffix4235); if (state.failed) return ;
                    match(input,30,FOLLOW_30_in_identifierSuffix4237); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:960:9: '.' explicitGenericInvocation
                    {
                    match(input,28,FOLLOW_28_in_identifierSuffix4247); if (state.failed) return ;
                    pushFollow(FOLLOW_explicitGenericInvocation_in_identifierSuffix4249);
                    explicitGenericInvocation();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:961:9: '.' 'this'
                    {
                    match(input,28,FOLLOW_28_in_identifierSuffix4259); if (state.failed) return ;
                    match(input,118,FOLLOW_118_in_identifierSuffix4261); if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:962:9: '.' 'super' arguments
                    {
                    match(input,28,FOLLOW_28_in_identifierSuffix4271); if (state.failed) return ;
                    match(input,64,FOLLOW_64_in_identifierSuffix4273); if (state.failed) return ;
                    pushFollow(FOLLOW_arguments_in_identifierSuffix4275);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:963:9: '.' 'new' ( nonWildcardTypeArguments )? innerCreator
                    {
                    match(input,28,FOLLOW_28_in_identifierSuffix4285); if (state.failed) return ;
                    match(input,119,FOLLOW_119_in_identifierSuffix4287); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:963:19: ( nonWildcardTypeArguments )?
                    int alt164=2;
                    int LA164_0 = input.LA(1);

                    if ( (LA164_0==33) ) {
                        alt164=1;
                    }
                    switch (alt164) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:963:20: nonWildcardTypeArguments
                            {
                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_identifierSuffix4290);
                            nonWildcardTypeArguments();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    pushFollow(FOLLOW_innerCreator_in_identifierSuffix4294);
                    innerCreator();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 123, identifierSuffix_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "identifierSuffix"


    // $ANTLR start "creator"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:966:1: creator : ( nonWildcardTypeArguments )? createdName ( arrayCreatorRest | classCreatorRest ) ;
    public final void creator() throws RecognitionException {
        int creator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 124) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:967:2: ( ( nonWildcardTypeArguments )? createdName ( arrayCreatorRest | classCreatorRest ) )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:967:4: ( nonWildcardTypeArguments )? createdName ( arrayCreatorRest | classCreatorRest )
            {
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:967:4: ( nonWildcardTypeArguments )?
            int alt166=2;
            int LA166_0 = input.LA(1);

            if ( (LA166_0==33) ) {
                alt166=1;
            }
            switch (alt166) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: nonWildcardTypeArguments
                    {
                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_creator4306);
                    nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_createdName_in_creator4309);
            createdName();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:968:9: ( arrayCreatorRest | classCreatorRest )
            int alt167=2;
            int LA167_0 = input.LA(1);

            if ( (LA167_0==41) ) {
                alt167=1;
            }
            else if ( (LA167_0==65) ) {
                alt167=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 167, 0, input);

                throw nvae;
            }
            switch (alt167) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:968:10: arrayCreatorRest
                    {
                    pushFollow(FOLLOW_arrayCreatorRest_in_creator4320);
                    arrayCreatorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:968:29: classCreatorRest
                    {
                    pushFollow(FOLLOW_classCreatorRest_in_creator4324);
                    classCreatorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 124, creator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "creator"


    // $ANTLR start "createdName"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:971:1: createdName : ( Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )* | primitiveType );
    public final void createdName() throws RecognitionException {
        int createdName_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 125) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:972:2: ( Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )* | primitiveType )
            int alt171=2;
            int LA171_0 = input.LA(1);

            if ( (LA171_0==Identifier) ) {
                alt171=1;
            }
            else if ( ((LA171_0>=55 && LA171_0<=62)) ) {
                alt171=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 171, 0, input);

                throw nvae;
            }
            switch (alt171) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:972:4: Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )*
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_createdName4336); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:972:15: ( typeArguments )?
                    int alt168=2;
                    int LA168_0 = input.LA(1);

                    if ( (LA168_0==33) ) {
                        alt168=1;
                    }
                    switch (alt168) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: typeArguments
                            {
                            pushFollow(FOLLOW_typeArguments_in_createdName4338);
                            typeArguments();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:973:9: ( '.' Identifier ( typeArguments )? )*
                    loop170:
                    do {
                        int alt170=2;
                        int LA170_0 = input.LA(1);

                        if ( (LA170_0==28) ) {
                            alt170=1;
                        }


                        switch (alt170) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:973:10: '.' Identifier ( typeArguments )?
                    	    {
                    	    match(input,28,FOLLOW_28_in_createdName4350); if (state.failed) return ;
                    	    match(input,Identifier,FOLLOW_Identifier_in_createdName4352); if (state.failed) return ;
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:973:25: ( typeArguments )?
                    	    int alt169=2;
                    	    int LA169_0 = input.LA(1);

                    	    if ( (LA169_0==33) ) {
                    	        alt169=1;
                    	    }
                    	    switch (alt169) {
                    	        case 1 :
                    	            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: typeArguments
                    	            {
                    	            pushFollow(FOLLOW_typeArguments_in_createdName4354);
                    	            typeArguments();

                    	            state._fsp--;
                    	            if (state.failed) return ;

                    	            }
                    	            break;

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop170;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:974:7: primitiveType
                    {
                    pushFollow(FOLLOW_primitiveType_in_createdName4365);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 125, createdName_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "createdName"


    // $ANTLR start "innerCreator"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:977:1: innerCreator : Identifier classCreatorRest ;
    public final void innerCreator() throws RecognitionException {
        int innerCreator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 126) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:978:2: ( Identifier classCreatorRest )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:978:4: Identifier classCreatorRest
            {
            match(input,Identifier,FOLLOW_Identifier_in_innerCreator4377); if (state.failed) return ;
            pushFollow(FOLLOW_classCreatorRest_in_innerCreator4379);
            classCreatorRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 126, innerCreator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "innerCreator"


    // $ANTLR start "arrayCreatorRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:981:1: arrayCreatorRest : '[' ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* ) ;
    public final void arrayCreatorRest() throws RecognitionException {
        int arrayCreatorRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 127) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:982:2: ( '[' ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* ) )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:982:4: '[' ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* )
            {
            match(input,41,FOLLOW_41_in_arrayCreatorRest4390); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:983:9: ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* )
            int alt175=2;
            alt175 = dfa175.predict(input);
            switch (alt175) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:983:13: ']' ( '[' ']' )* arrayInitializer
                    {
                    match(input,42,FOLLOW_42_in_arrayCreatorRest4404); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:983:17: ( '[' ']' )*
                    loop172:
                    do {
                        int alt172=2;
                        int LA172_0 = input.LA(1);

                        if ( (LA172_0==41) ) {
                            alt172=1;
                        }


                        switch (alt172) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:983:18: '[' ']'
                    	    {
                    	    match(input,41,FOLLOW_41_in_arrayCreatorRest4407); if (state.failed) return ;
                    	    match(input,42,FOLLOW_42_in_arrayCreatorRest4409); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop172;
                        }
                    } while (true);

                    pushFollow(FOLLOW_arrayInitializer_in_arrayCreatorRest4413);
                    arrayInitializer();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:984:13: expression ']' ( '[' expression ']' )* ( '[' ']' )*
                    {
                    pushFollow(FOLLOW_expression_in_arrayCreatorRest4427);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,42,FOLLOW_42_in_arrayCreatorRest4429); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:984:28: ( '[' expression ']' )*
                    loop173:
                    do {
                        int alt173=2;
                        alt173 = dfa173.predict(input);
                        switch (alt173) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:984:29: '[' expression ']'
                    	    {
                    	    match(input,41,FOLLOW_41_in_arrayCreatorRest4432); if (state.failed) return ;
                    	    pushFollow(FOLLOW_expression_in_arrayCreatorRest4434);
                    	    expression();

                    	    state._fsp--;
                    	    if (state.failed) return ;
                    	    match(input,42,FOLLOW_42_in_arrayCreatorRest4436); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop173;
                        }
                    } while (true);

                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:984:50: ( '[' ']' )*
                    loop174:
                    do {
                        int alt174=2;
                        alt174 = dfa174.predict(input);
                        switch (alt174) {
                    	case 1 :
                    	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:984:51: '[' ']'
                    	    {
                    	    match(input,41,FOLLOW_41_in_arrayCreatorRest4441); if (state.failed) return ;
                    	    match(input,42,FOLLOW_42_in_arrayCreatorRest4443); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop174;
                        }
                    } while (true);


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 127, arrayCreatorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "arrayCreatorRest"


    // $ANTLR start "classCreatorRest"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:988:1: classCreatorRest : arguments ( classBody )? ;
    public final void classCreatorRest() throws RecognitionException {
        int classCreatorRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 128) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:989:2: ( arguments ( classBody )? )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:989:4: arguments ( classBody )?
            {
            pushFollow(FOLLOW_arguments_in_classCreatorRest4466);
            arguments();

            state._fsp--;
            if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:989:14: ( classBody )?
            int alt176=2;
            alt176 = dfa176.predict(input);
            switch (alt176) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: classBody
                    {
                    pushFollow(FOLLOW_classBody_in_classCreatorRest4468);
                    classBody();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 128, classCreatorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "classCreatorRest"


    // $ANTLR start "explicitGenericInvocation"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:992:1: explicitGenericInvocation : nonWildcardTypeArguments explicitGenericInvocationSuffix ;
    public final void explicitGenericInvocation() throws RecognitionException {
        int explicitGenericInvocation_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 129) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:993:2: ( nonWildcardTypeArguments explicitGenericInvocationSuffix )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:993:4: nonWildcardTypeArguments explicitGenericInvocationSuffix
            {
            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitGenericInvocation4481);
            nonWildcardTypeArguments();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_explicitGenericInvocationSuffix_in_explicitGenericInvocation4483);
            explicitGenericInvocationSuffix();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 129, explicitGenericInvocation_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "explicitGenericInvocation"


    // $ANTLR start "nonWildcardTypeArguments"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:996:1: nonWildcardTypeArguments : '<' typeList '>' ;
    public final void nonWildcardTypeArguments() throws RecognitionException {
        int nonWildcardTypeArguments_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 130) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:997:2: ( '<' typeList '>' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:997:4: '<' typeList '>'
            {
            match(input,33,FOLLOW_33_in_nonWildcardTypeArguments4495); if (state.failed) return ;
            pushFollow(FOLLOW_typeList_in_nonWildcardTypeArguments4497);
            typeList();

            state._fsp--;
            if (state.failed) return ;
            match(input,35,FOLLOW_35_in_nonWildcardTypeArguments4499); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 130, nonWildcardTypeArguments_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "nonWildcardTypeArguments"


    // $ANTLR start "explicitGenericInvocationSuffix"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1000:1: explicitGenericInvocationSuffix : ( 'super' superSuffix | Identifier arguments );
    public final void explicitGenericInvocationSuffix() throws RecognitionException {
        int explicitGenericInvocationSuffix_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 131) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1001:2: ( 'super' superSuffix | Identifier arguments )
            int alt177=2;
            int LA177_0 = input.LA(1);

            if ( (LA177_0==64) ) {
                alt177=1;
            }
            else if ( (LA177_0==Identifier) ) {
                alt177=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 177, 0, input);

                throw nvae;
            }
            switch (alt177) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1001:4: 'super' superSuffix
                    {
                    match(input,64,FOLLOW_64_in_explicitGenericInvocationSuffix4511); if (state.failed) return ;
                    pushFollow(FOLLOW_superSuffix_in_explicitGenericInvocationSuffix4513);
                    superSuffix();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1002:6: Identifier arguments
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_explicitGenericInvocationSuffix4520); if (state.failed) return ;
                    pushFollow(FOLLOW_arguments_in_explicitGenericInvocationSuffix4522);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 131, explicitGenericInvocationSuffix_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "explicitGenericInvocationSuffix"


    // $ANTLR start "selector"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1005:1: selector : ( '.' Identifier ( arguments )? | '.' 'this' | '.' 'super' superSuffix | '.' 'new' ( nonWildcardTypeArguments )? innerCreator | '[' expression ']' );
    public final void selector() throws RecognitionException {
        int selector_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 132) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1006:2: ( '.' Identifier ( arguments )? | '.' 'this' | '.' 'super' superSuffix | '.' 'new' ( nonWildcardTypeArguments )? innerCreator | '[' expression ']' )
            int alt180=5;
            int LA180_0 = input.LA(1);

            if ( (LA180_0==28) ) {
                switch ( input.LA(2) ) {
                case Identifier:
                    {
                    alt180=1;
                    }
                    break;
                case 118:
                    {
                    alt180=2;
                    }
                    break;
                case 64:
                    {
                    alt180=3;
                    }
                    break;
                case 119:
                    {
                    alt180=4;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 180, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA180_0==41) ) {
                alt180=5;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 180, 0, input);

                throw nvae;
            }
            switch (alt180) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1006:4: '.' Identifier ( arguments )?
                    {
                    match(input,28,FOLLOW_28_in_selector4534); if (state.failed) return ;
                    match(input,Identifier,FOLLOW_Identifier_in_selector4536); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1006:19: ( arguments )?
                    int alt178=2;
                    alt178 = dfa178.predict(input);
                    switch (alt178) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1006:20: arguments
                            {
                            pushFollow(FOLLOW_arguments_in_selector4539);
                            arguments();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1007:6: '.' 'this'
                    {
                    match(input,28,FOLLOW_28_in_selector4548); if (state.failed) return ;
                    match(input,118,FOLLOW_118_in_selector4550); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1008:6: '.' 'super' superSuffix
                    {
                    match(input,28,FOLLOW_28_in_selector4557); if (state.failed) return ;
                    match(input,64,FOLLOW_64_in_selector4559); if (state.failed) return ;
                    pushFollow(FOLLOW_superSuffix_in_selector4561);
                    superSuffix();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1009:6: '.' 'new' ( nonWildcardTypeArguments )? innerCreator
                    {
                    match(input,28,FOLLOW_28_in_selector4568); if (state.failed) return ;
                    match(input,119,FOLLOW_119_in_selector4570); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1009:16: ( nonWildcardTypeArguments )?
                    int alt179=2;
                    int LA179_0 = input.LA(1);

                    if ( (LA179_0==33) ) {
                        alt179=1;
                    }
                    switch (alt179) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1009:17: nonWildcardTypeArguments
                            {
                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_selector4573);
                            nonWildcardTypeArguments();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    pushFollow(FOLLOW_innerCreator_in_selector4577);
                    innerCreator();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1010:6: '[' expression ']'
                    {
                    match(input,41,FOLLOW_41_in_selector4584); if (state.failed) return ;
                    pushFollow(FOLLOW_expression_in_selector4586);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,42,FOLLOW_42_in_selector4588); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 132, selector_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "selector"


    // $ANTLR start "superSuffix"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1013:1: superSuffix : ( arguments | '.' Identifier ( arguments )? );
    public final void superSuffix() throws RecognitionException {
        int superSuffix_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 133) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1014:2: ( arguments | '.' Identifier ( arguments )? )
            int alt182=2;
            int LA182_0 = input.LA(1);

            if ( (LA182_0==65) ) {
                alt182=1;
            }
            else if ( (LA182_0==28) ) {
                alt182=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 182, 0, input);

                throw nvae;
            }
            switch (alt182) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1014:4: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_superSuffix4600);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1015:6: '.' Identifier ( arguments )?
                    {
                    match(input,28,FOLLOW_28_in_superSuffix4607); if (state.failed) return ;
                    match(input,Identifier,FOLLOW_Identifier_in_superSuffix4609); if (state.failed) return ;
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1015:21: ( arguments )?
                    int alt181=2;
                    alt181 = dfa181.predict(input);
                    switch (alt181) {
                        case 1 :
                            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1015:22: arguments
                            {
                            pushFollow(FOLLOW_arguments_in_superSuffix4612);
                            arguments();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 133, superSuffix_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "superSuffix"


    // $ANTLR start "arguments"
    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1018:1: arguments : '(' ( expressionList )? ')' ;
    public final void arguments() throws RecognitionException {
        int arguments_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 134) ) { return ; }
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1019:2: ( '(' ( expressionList )? ')' )
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1019:4: '(' ( expressionList )? ')'
            {
            match(input,65,FOLLOW_65_in_arguments4628); if (state.failed) return ;
            // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:1019:8: ( expressionList )?
            int alt183=2;
            alt183 = dfa183.predict(input);
            switch (alt183) {
                case 1 :
                    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: expressionList
                    {
                    pushFollow(FOLLOW_expressionList_in_arguments4630);
                    expressionList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            match(input,66,FOLLOW_66_in_arguments4633); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 134, arguments_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "arguments"

    // $ANTLR start synpred1_Java
    public final void synpred1_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:207:4: ( annotations )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:207:4: annotations
        {
        pushFollow(FOLLOW_annotations_in_synpred1_Java70);
        annotations();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_Java

    // $ANTLR start synpred38_Java
    public final void synpred38_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:303:4: ( methodDeclaration )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:303:4: methodDeclaration
        {
        pushFollow(FOLLOW_methodDeclaration_in_synpred38_Java577);
        methodDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred38_Java

    // $ANTLR start synpred39_Java
    public final void synpred39_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:304:4: ( fieldDeclaration )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:304:4: fieldDeclaration
        {
        pushFollow(FOLLOW_fieldDeclaration_in_synpred39_Java582);
        fieldDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred39_Java

    // $ANTLR start synpred85_Java
    public final void synpred85_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:459:16: ( '.' Identifier )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:459:16: '.' Identifier
        {
        match(input,28,FOLLOW_28_in_synpred85_Java1389); if (state.failed) return ;
        match(input,Identifier,FOLLOW_Identifier_in_synpred85_Java1391); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred85_Java

    // $ANTLR start synpred120_Java
    public final void synpred120_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:549:4: ( annotation )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:549:4: annotation
        {
        pushFollow(FOLLOW_annotation_in_synpred120_Java1893);
        annotation();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred120_Java

    // $ANTLR start synpred135_Java
    public final void synpred135_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:596:6: ( classDeclaration ( ';' )? )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:596:6: classDeclaration ( ';' )?
        {
        pushFollow(FOLLOW_classDeclaration_in_synpred135_Java2123);
        classDeclaration();

        state._fsp--;
        if (state.failed) return ;
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:596:23: ( ';' )?
        int alt199=2;
        int LA199_0 = input.LA(1);

        if ( (LA199_0==25) ) {
            alt199=1;
        }
        switch (alt199) {
            case 1 :
                // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: ';'
                {
                match(input,25,FOLLOW_25_in_synpred135_Java2125); if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred135_Java

    // $ANTLR start synpred137_Java
    public final void synpred137_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:597:6: ( interfaceDeclaration ( ';' )? )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:597:6: interfaceDeclaration ( ';' )?
        {
        pushFollow(FOLLOW_interfaceDeclaration_in_synpred137_Java2133);
        interfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:597:27: ( ';' )?
        int alt200=2;
        int LA200_0 = input.LA(1);

        if ( (LA200_0==25) ) {
            alt200=1;
        }
        switch (alt200) {
            case 1 :
                // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: ';'
                {
                match(input,25,FOLLOW_25_in_synpred137_Java2135); if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred137_Java

    // $ANTLR start synpred139_Java
    public final void synpred139_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:598:6: ( enumDeclaration ( ';' )? )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:598:6: enumDeclaration ( ';' )?
        {
        pushFollow(FOLLOW_enumDeclaration_in_synpred139_Java2143);
        enumDeclaration();

        state._fsp--;
        if (state.failed) return ;
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:598:22: ( ';' )?
        int alt201=2;
        int LA201_0 = input.LA(1);

        if ( (LA201_0==25) ) {
            alt201=1;
        }
        switch (alt201) {
            case 1 :
                // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: ';'
                {
                match(input,25,FOLLOW_25_in_synpred139_Java2145); if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred139_Java

    // $ANTLR start synpred144_Java
    public final void synpred144_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:632:4: ( localVariableDeclaration )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:632:4: localVariableDeclaration
        {
        pushFollow(FOLLOW_localVariableDeclaration_in_synpred144_Java2286);
        localVariableDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred144_Java

    // $ANTLR start synpred145_Java
    public final void synpred145_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:633:4: ( classOrInterfaceDeclaration )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:633:4: classOrInterfaceDeclaration
        {
        pushFollow(FOLLOW_classOrInterfaceDeclaration_in_synpred145_Java2291);
        classOrInterfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred145_Java

    // $ANTLR start synpred150_Java
    public final void synpred150_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:666:52: ( 'else' statement )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:666:52: 'else' statement
        {
        match(input,76,FOLLOW_76_in_synpred150_Java2431); if (state.failed) return ;
        pushFollow(FOLLOW_statement_in_synpred150_Java2433);
        statement();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred150_Java

    // $ANTLR start synpred155_Java
    public final void synpred155_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:671:9: ( catches 'finally' block )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:671:9: catches 'finally' block
        {
        pushFollow(FOLLOW_catches_in_synpred155_Java2499);
        catches();

        state._fsp--;
        if (state.failed) return ;
        match(input,81,FOLLOW_81_in_synpred155_Java2501); if (state.failed) return ;
        pushFollow(FOLLOW_block_in_synpred155_Java2503);
        block();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred155_Java

    // $ANTLR start synpred156_Java
    public final void synpred156_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:672:9: ( catches )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:672:9: catches
        {
        pushFollow(FOLLOW_catches_in_synpred156_Java2513);
        catches();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred156_Java

    // $ANTLR start synpred178_Java
    public final void synpred178_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:792:4: ( 'case' constantExpression ':' )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:792:4: 'case' constantExpression ':'
        {
        match(input,94,FOLLOW_94_in_synpred178_Java3056); if (state.failed) return ;
        pushFollow(FOLLOW_constantExpression_in_synpred178_Java3058);
        constantExpression();

        state._fsp--;
        if (state.failed) return ;
        match(input,74,FOLLOW_74_in_synpred178_Java3060); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred178_Java

    // $ANTLR start synpred179_Java
    public final void synpred179_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:793:6: ( 'case' enumConstantName ':' )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:793:6: 'case' enumConstantName ':'
        {
        match(input,94,FOLLOW_94_in_synpred179_Java3067); if (state.failed) return ;
        pushFollow(FOLLOW_enumConstantName_in_synpred179_Java3069);
        enumConstantName();

        state._fsp--;
        if (state.failed) return ;
        match(input,74,FOLLOW_74_in_synpred179_Java3071); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred179_Java

    // $ANTLR start synpred181_Java
    public final void synpred181_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:803:4: ( forVarControl )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:803:4: forVarControl
        {
        pushFollow(FOLLOW_forVarControl_in_synpred181_Java3116);
        forVarControl();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred181_Java

    // $ANTLR start synpred186_Java
    public final void synpred186_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:814:4: ( ( variableModifier )* type variableDeclarators )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:814:4: ( variableModifier )* type variableDeclarators
        {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:814:4: ( variableModifier )*
        loop209:
        do {
            int alt209=2;
            int LA209_0 = input.LA(1);

            if ( (LA209_0==49||LA209_0==71) ) {
                alt209=1;
            }


            switch (alt209) {
        	case 1 :
        	    // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:0:0: variableModifier
        	    {
        	    pushFollow(FOLLOW_variableModifier_in_synpred186_Java3169);
        	    variableModifier();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop209;
            }
        } while (true);

        pushFollow(FOLLOW_type_in_synpred186_Java3172);
        type();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_variableDeclarators_in_synpred186_Java3174);
        variableDeclarators();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred186_Java

    // $ANTLR start synpred189_Java
    public final void synpred189_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:845:27: ( assignmentOperator expression )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:845:27: assignmentOperator expression
        {
        pushFollow(FOLLOW_assignmentOperator_in_synpred189_Java3293);
        assignmentOperator();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_expression_in_synpred189_Java3295);
        expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred189_Java

    // $ANTLR start synpred200_Java
    public final void synpred200_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:859:9: ( '>' '>' '=' )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:859:9: '>' '>' '='
        {
        match(input,35,FOLLOW_35_in_synpred200_Java3413); if (state.failed) return ;
        match(input,35,FOLLOW_35_in_synpred200_Java3415); if (state.failed) return ;
        match(input,44,FOLLOW_44_in_synpred200_Java3417); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred200_Java

    // $ANTLR start synpred210_Java
    public final void synpred210_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:896:27: ( relationalOp shiftExpression )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:896:27: relationalOp shiftExpression
        {
        pushFollow(FOLLOW_relationalOp_in_synpred210_Java3661);
        relationalOp();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_shiftExpression_in_synpred210_Java3663);
        shiftExpression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred210_Java

    // $ANTLR start synpred214_Java
    public final void synpred214_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:904:30: ( shiftOp additiveExpression )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:904:30: shiftOp additiveExpression
        {
        pushFollow(FOLLOW_shiftOp_in_synpred214_Java3716);
        shiftOp();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_additiveExpression_in_synpred214_Java3718);
        additiveExpression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred214_Java

    // $ANTLR start synpred216_Java
    public final void synpred216_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:909:15: ( '>' '>' '>' )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:909:15: '>' '>' '>'
        {
        match(input,35,FOLLOW_35_in_synpred216_Java3748); if (state.failed) return ;
        match(input,35,FOLLOW_35_in_synpred216_Java3750); if (state.failed) return ;
        match(input,35,FOLLOW_35_in_synpred216_Java3752); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred216_Java

    // $ANTLR start synpred228_Java
    public final void synpred228_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:932:9: ( castExpression )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:932:9: castExpression
        {
        pushFollow(FOLLOW_castExpression_in_synpred228_Java3933);
        castExpression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred228_Java

    // $ANTLR start synpred232_Java
    public final void synpred232_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:937:8: ( '(' primitiveType ')' unaryExpression )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:937:8: '(' primitiveType ')' unaryExpression
        {
        match(input,65,FOLLOW_65_in_synpred232_Java3971); if (state.failed) return ;
        pushFollow(FOLLOW_primitiveType_in_synpred232_Java3973);
        primitiveType();

        state._fsp--;
        if (state.failed) return ;
        match(input,66,FOLLOW_66_in_synpred232_Java3975); if (state.failed) return ;
        pushFollow(FOLLOW_unaryExpression_in_synpred232_Java3977);
        unaryExpression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred232_Java

    // $ANTLR start synpred233_Java
    public final void synpred233_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:938:13: ( type )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:938:13: type
        {
        pushFollow(FOLLOW_type_in_synpred233_Java3989);
        type();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred233_Java

    // $ANTLR start synpred237_Java
    public final void synpred237_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:945:17: ( '.' Identifier )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:945:17: '.' Identifier
        {
        match(input,28,FOLLOW_28_in_synpred237_Java4056); if (state.failed) return ;
        match(input,Identifier,FOLLOW_Identifier_in_synpred237_Java4058); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred237_Java

    // $ANTLR start synpred238_Java
    public final void synpred238_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:945:35: ( identifierSuffix )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:945:35: identifierSuffix
        {
        pushFollow(FOLLOW_identifierSuffix_in_synpred238_Java4063);
        identifierSuffix();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred238_Java

    // $ANTLR start synpred241_Java
    public final void synpred241_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:947:22: ( '.' Identifier )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:947:22: '.' Identifier
        {
        match(input,28,FOLLOW_28_in_synpred241_Java4090); if (state.failed) return ;
        match(input,Identifier,FOLLOW_Identifier_in_synpred241_Java4092); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred241_Java

    // $ANTLR start synpred242_Java
    public final void synpred242_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:947:40: ( identifierSuffix )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:947:40: identifierSuffix
        {
        pushFollow(FOLLOW_identifierSuffix_in_synpred242_Java4097);
        identifierSuffix();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred242_Java

    // $ANTLR start synpred246_Java
    public final void synpred246_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:950:127: ( '.' Identifier )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:950:127: '.' Identifier
        {
        match(input,28,FOLLOW_28_in_synpred246_Java4138); if (state.failed) return ;
        match(input,Identifier,FOLLOW_Identifier_in_synpred246_Java4140); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred246_Java

    // $ANTLR start synpred247_Java
    public final void synpred247_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:950:145: ( identifierSuffix )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:950:145: identifierSuffix
        {
        pushFollow(FOLLOW_identifierSuffix_in_synpred247_Java4145);
        identifierSuffix();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred247_Java

    // $ANTLR start synpred253_Java
    public final void synpred253_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:957:5: ( '[' expression ']' )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:957:5: '[' expression ']'
        {
        match(input,41,FOLLOW_41_in_synpred253_Java4208); if (state.failed) return ;
        pushFollow(FOLLOW_expression_in_synpred253_Java4210);
        expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,42,FOLLOW_42_in_synpred253_Java4212); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred253_Java

    // $ANTLR start synpred269_Java
    public final void synpred269_Java_fragment() throws RecognitionException {
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:984:29: ( '[' expression ']' )
        // C:\\dev\\drools\\git\\refactor-memory-iteration\\drools-compiler\\src\\main\\resources\\org\\drools\\semantics\\java\\parser\\Java.g:984:29: '[' expression ']'
        {
        match(input,41,FOLLOW_41_in_synpred269_Java4432); if (state.failed) return ;
        pushFollow(FOLLOW_expression_in_synpred269_Java4434);
        expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,42,FOLLOW_42_in_synpred269_Java4436); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred269_Java

    // Delegated rules

    public final boolean synpred139_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred139_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred178_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred178_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred214_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred214_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred210_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred210_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred246_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred246_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred253_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred253_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred189_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred189_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred144_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred144_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred238_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred238_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred186_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred186_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred137_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred137_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred38_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred38_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred269_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred269_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred145_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred145_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred155_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred155_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred39_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred39_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred242_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred242_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred156_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred156_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred216_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred216_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred247_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred247_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred120_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred120_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred233_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred233_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred241_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred241_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred150_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred150_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred85_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred85_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred179_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred179_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred200_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred200_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred135_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred135_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred228_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred228_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred181_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred181_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred237_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred237_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred232_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred232_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA1 dfa1 = new DFA1(this);
    protected DFA2 dfa2 = new DFA2(this);
    protected DFA3 dfa3 = new DFA3(this);
    protected DFA4 dfa4 = new DFA4(this);
    protected DFA8 dfa8 = new DFA8(this);
    protected DFA9 dfa9 = new DFA9(this);
    protected DFA26 dfa26 = new DFA26(this);
    protected DFA31 dfa31 = new DFA31(this);
    protected DFA32 dfa32 = new DFA32(this);
    protected DFA35 dfa35 = new DFA35(this);
    protected DFA34 dfa34 = new DFA34(this);
    protected DFA36 dfa36 = new DFA36(this);
    protected DFA40 dfa40 = new DFA40(this);
    protected DFA39 dfa39 = new DFA39(this);
    protected DFA60 dfa60 = new DFA60(this);
    protected DFA63 dfa63 = new DFA63(this);
    protected DFA61 dfa61 = new DFA61(this);
    protected DFA64 dfa64 = new DFA64(this);
    protected DFA67 dfa67 = new DFA67(this);
    protected DFA69 dfa69 = new DFA69(this);
    protected DFA68 dfa68 = new DFA68(this);
    protected DFA70 dfa70 = new DFA70(this);
    protected DFA71 dfa71 = new DFA71(this);
    protected DFA85 dfa85 = new DFA85(this);
    protected DFA87 dfa87 = new DFA87(this);
    protected DFA86 dfa86 = new DFA86(this);
    protected DFA88 dfa88 = new DFA88(this);
    protected DFA90 dfa90 = new DFA90(this);
    protected DFA91 dfa91 = new DFA91(this);
    protected DFA93 dfa93 = new DFA93(this);
    protected DFA94 dfa94 = new DFA94(this);
    protected DFA95 dfa95 = new DFA95(this);
    protected DFA96 dfa96 = new DFA96(this);
    protected DFA101 dfa101 = new DFA101(this);
    protected DFA97 dfa97 = new DFA97(this);
    protected DFA98 dfa98 = new DFA98(this);
    protected DFA99 dfa99 = new DFA99(this);
    protected DFA100 dfa100 = new DFA100(this);
    protected DFA104 dfa104 = new DFA104(this);
    protected DFA105 dfa105 = new DFA105(this);
    protected DFA113 dfa113 = new DFA113(this);
    protected DFA110 dfa110 = new DFA110(this);
    protected DFA115 dfa115 = new DFA115(this);
    protected DFA117 dfa117 = new DFA117(this);
    protected DFA120 dfa120 = new DFA120(this);
    protected DFA121 dfa121 = new DFA121(this);
    protected DFA126 dfa126 = new DFA126(this);
    protected DFA123 dfa123 = new DFA123(this);
    protected DFA124 dfa124 = new DFA124(this);
    protected DFA125 dfa125 = new DFA125(this);
    protected DFA128 dfa128 = new DFA128(this);
    protected DFA131 dfa131 = new DFA131(this);
    protected DFA132 dfa132 = new DFA132(this);
    protected DFA133 dfa133 = new DFA133(this);
    protected DFA134 dfa134 = new DFA134(this);
    protected DFA135 dfa135 = new DFA135(this);
    protected DFA136 dfa136 = new DFA136(this);
    protected DFA137 dfa137 = new DFA137(this);
    protected DFA138 dfa138 = new DFA138(this);
    protected DFA139 dfa139 = new DFA139(this);
    protected DFA140 dfa140 = new DFA140(this);
    protected DFA141 dfa141 = new DFA141(this);
    protected DFA142 dfa142 = new DFA142(this);
    protected DFA143 dfa143 = new DFA143(this);
    protected DFA145 dfa145 = new DFA145(this);
    protected DFA146 dfa146 = new DFA146(this);
    protected DFA147 dfa147 = new DFA147(this);
    protected DFA150 dfa150 = new DFA150(this);
    protected DFA148 dfa148 = new DFA148(this);
    protected DFA149 dfa149 = new DFA149(this);
    protected DFA152 dfa152 = new DFA152(this);
    protected DFA151 dfa151 = new DFA151(this);
    protected DFA161 dfa161 = new DFA161(this);
    protected DFA154 dfa154 = new DFA154(this);
    protected DFA155 dfa155 = new DFA155(this);
    protected DFA156 dfa156 = new DFA156(this);
    protected DFA157 dfa157 = new DFA157(this);
    protected DFA158 dfa158 = new DFA158(this);
    protected DFA159 dfa159 = new DFA159(this);
    protected DFA165 dfa165 = new DFA165(this);
    protected DFA163 dfa163 = new DFA163(this);
    protected DFA175 dfa175 = new DFA175(this);
    protected DFA173 dfa173 = new DFA173(this);
    protected DFA174 dfa174 = new DFA174(this);
    protected DFA176 dfa176 = new DFA176(this);
    protected DFA178 dfa178 = new DFA178(this);
    protected DFA181 dfa181 = new DFA181(this);
    protected DFA183 dfa183 = new DFA183(this);
    static final String DFA1_eotS =
        "\27\uffff";
    static final String DFA1_eofS =
        "\1\2\26\uffff";
    static final String DFA1_minS =
        "\1\5\1\4\23\uffff\1\0\1\uffff";
    static final String DFA1_maxS =
        "\1\107\1\47\23\uffff\1\0\1\uffff";
    static final String DFA1_acceptS =
        "\2\uffff\1\2\23\uffff\1\1";
    static final String DFA1_specialS =
        "\25\uffff\1\0\1\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\2\22\uffff\4\2\2\uffff\1\2\10\uffff\1\2\5\uffff\12\2\20"+
            "\uffff\1\1",
            "\1\25\42\uffff\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            ""
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "207:4: ( annotations )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA1_21 = input.LA(1);

                         
                        int index1_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_Java()) ) {s = 22;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index1_21);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 1, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA2_eotS =
        "\24\uffff";
    static final String DFA2_eofS =
        "\1\2\23\uffff";
    static final String DFA2_minS =
        "\1\5\23\uffff";
    static final String DFA2_maxS =
        "\1\107\23\uffff";
    static final String DFA2_acceptS =
        "\1\uffff\1\1\1\2\21\uffff";
    static final String DFA2_specialS =
        "\24\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\2\22\uffff\1\1\3\2\2\uffff\1\2\10\uffff\1\2\5\uffff\12\2"+
            "\20\uffff\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "208:3: ( packageDeclaration )?";
        }
    }
    static final String DFA3_eotS =
        "\23\uffff";
    static final String DFA3_eofS =
        "\1\1\22\uffff";
    static final String DFA3_minS =
        "\1\5\22\uffff";
    static final String DFA3_maxS =
        "\1\107\22\uffff";
    static final String DFA3_acceptS =
        "\1\uffff\1\2\20\uffff\1\1";
    static final String DFA3_specialS =
        "\23\uffff}>";
    static final String[] DFA3_transitionS = {
            "\1\1\23\uffff\1\1\1\22\1\1\2\uffff\1\1\10\uffff\1\1\5\uffff"+
            "\12\1\20\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "()* loopback of 209:9: ( importDeclaration )*";
        }
    }
    static final String DFA4_eotS =
        "\22\uffff";
    static final String DFA4_eofS =
        "\1\1\21\uffff";
    static final String DFA4_minS =
        "\1\5\21\uffff";
    static final String DFA4_maxS =
        "\1\107\21\uffff";
    static final String DFA4_acceptS =
        "\1\uffff\1\2\1\1\17\uffff";
    static final String DFA4_specialS =
        "\22\uffff}>";
    static final String[] DFA4_transitionS = {
            "\1\2\23\uffff\1\2\1\uffff\1\2\2\uffff\1\2\10\uffff\1\2\5\uffff"+
            "\12\2\20\uffff\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }
        public String getDescription() {
            return "()* loopback of 210:9: ( typeDeclaration )*";
        }
    }
    static final String DFA8_eotS =
        "\21\uffff";
    static final String DFA8_eofS =
        "\21\uffff";
    static final String DFA8_minS =
        "\1\5\20\uffff";
    static final String DFA8_maxS =
        "\1\107\20\uffff";
    static final String DFA8_acceptS =
        "\1\uffff\1\1\16\uffff\1\2";
    static final String DFA8_specialS =
        "\21\uffff}>";
    static final String[] DFA8_transitionS = {
            "\1\1\23\uffff\1\20\1\uffff\1\1\2\uffff\1\1\10\uffff\1\1\5\uffff"+
            "\12\1\20\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
    static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
    static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
    static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
    static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
    static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
    static final short[][] DFA8_transition;

    static {
        int numStates = DFA8_transitionS.length;
        DFA8_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
        }
    }

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA8_eot;
            this.eof = DFA8_eof;
            this.min = DFA8_min;
            this.max = DFA8_max;
            this.accept = DFA8_accept;
            this.special = DFA8_special;
            this.transition = DFA8_transition;
        }
        public String getDescription() {
            return "221:1: typeDeclaration : ( classOrInterfaceDeclaration | ';' );";
        }
    }
    static final String DFA9_eotS =
        "\22\uffff";
    static final String DFA9_eofS =
        "\22\uffff";
    static final String DFA9_minS =
        "\1\5\3\uffff\1\4\15\uffff";
    static final String DFA9_maxS =
        "\1\107\3\uffff\1\47\15\uffff";
    static final String DFA9_acceptS =
        "\1\uffff\1\2\3\uffff\1\1\14\uffff";
    static final String DFA9_specialS =
        "\22\uffff}>";
    static final String[] DFA9_transitionS = {
            "\1\1\25\uffff\1\5\2\uffff\1\1\10\uffff\1\1\5\uffff\12\5\20"+
            "\uffff\1\4",
            "",
            "",
            "",
            "\1\5\42\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "()* loopback of 227:4: ( modifier )*";
        }
    }
    static final String DFA26_eotS =
        "\30\uffff";
    static final String DFA26_eofS =
        "\1\1\27\uffff";
    static final String DFA26_minS =
        "\1\4\27\uffff";
    static final String DFA26_maxS =
        "\1\107\27\uffff";
    static final String DFA26_acceptS =
        "\1\uffff\1\2\1\uffff\1\1\24\uffff";
    static final String DFA26_specialS =
        "\30\uffff}>";
    static final String[] DFA26_transitionS = {
            "\2\3\23\uffff\1\3\1\uffff\1\3\2\uffff\1\3\2\uffff\1\3\3\uffff"+
            "\1\3\1\1\2\3\4\uffff\22\3\10\uffff\1\3",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA26_eot = DFA.unpackEncodedString(DFA26_eotS);
    static final short[] DFA26_eof = DFA.unpackEncodedString(DFA26_eofS);
    static final char[] DFA26_min = DFA.unpackEncodedStringToUnsignedChars(DFA26_minS);
    static final char[] DFA26_max = DFA.unpackEncodedStringToUnsignedChars(DFA26_maxS);
    static final short[] DFA26_accept = DFA.unpackEncodedString(DFA26_acceptS);
    static final short[] DFA26_special = DFA.unpackEncodedString(DFA26_specialS);
    static final short[][] DFA26_transition;

    static {
        int numStates = DFA26_transitionS.length;
        DFA26_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA26_transition[i] = DFA.unpackEncodedString(DFA26_transitionS[i]);
        }
    }

    class DFA26 extends DFA {

        public DFA26(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 26;
            this.eot = DFA26_eot;
            this.eof = DFA26_eof;
            this.min = DFA26_min;
            this.max = DFA26_max;
            this.accept = DFA26_accept;
            this.special = DFA26_special;
            this.transition = DFA26_transition;
        }
        public String getDescription() {
            return "()* loopback of 271:8: ( classBodyDeclaration )*";
        }
    }
    static final String DFA31_eotS =
        "\27\uffff";
    static final String DFA31_eofS =
        "\27\uffff";
    static final String DFA31_minS =
        "\1\4\26\uffff";
    static final String DFA31_maxS =
        "\1\107\26\uffff";
    static final String DFA31_acceptS =
        "\1\uffff\1\2\1\1\24\uffff";
    static final String DFA31_specialS =
        "\27\uffff}>";
    static final String[] DFA31_transitionS = {
            "\2\2\23\uffff\1\2\1\uffff\1\2\2\uffff\1\2\2\uffff\1\2\3\uffff"+
            "\1\2\1\1\2\2\4\uffff\22\2\10\uffff\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA31_eot = DFA.unpackEncodedString(DFA31_eotS);
    static final short[] DFA31_eof = DFA.unpackEncodedString(DFA31_eofS);
    static final char[] DFA31_min = DFA.unpackEncodedStringToUnsignedChars(DFA31_minS);
    static final char[] DFA31_max = DFA.unpackEncodedStringToUnsignedChars(DFA31_maxS);
    static final short[] DFA31_accept = DFA.unpackEncodedString(DFA31_acceptS);
    static final short[] DFA31_special = DFA.unpackEncodedString(DFA31_specialS);
    static final short[][] DFA31_transition;

    static {
        int numStates = DFA31_transitionS.length;
        DFA31_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA31_transition[i] = DFA.unpackEncodedString(DFA31_transitionS[i]);
        }
    }

    class DFA31 extends DFA {

        public DFA31(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 31;
            this.eot = DFA31_eot;
            this.eof = DFA31_eof;
            this.min = DFA31_min;
            this.max = DFA31_max;
            this.accept = DFA31_accept;
            this.special = DFA31_special;
            this.transition = DFA31_transition;
        }
        public String getDescription() {
            return "()* loopback of 288:8: ( classBodyDeclaration )*";
        }
    }
    static final String DFA32_eotS =
        "\26\uffff";
    static final String DFA32_eofS =
        "\26\uffff";
    static final String DFA32_minS =
        "\1\4\25\uffff";
    static final String DFA32_maxS =
        "\1\107\25\uffff";
    static final String DFA32_acceptS =
        "\1\uffff\1\2\1\1\23\uffff";
    static final String DFA32_specialS =
        "\26\uffff}>";
    static final String[] DFA32_transitionS = {
            "\2\2\23\uffff\1\2\1\uffff\1\2\2\uffff\1\2\2\uffff\1\2\4\uffff"+
            "\1\1\2\2\4\uffff\22\2\10\uffff\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA32_eot = DFA.unpackEncodedString(DFA32_eotS);
    static final short[] DFA32_eof = DFA.unpackEncodedString(DFA32_eofS);
    static final char[] DFA32_min = DFA.unpackEncodedStringToUnsignedChars(DFA32_minS);
    static final char[] DFA32_max = DFA.unpackEncodedStringToUnsignedChars(DFA32_maxS);
    static final short[] DFA32_accept = DFA.unpackEncodedString(DFA32_acceptS);
    static final short[] DFA32_special = DFA.unpackEncodedString(DFA32_specialS);
    static final short[][] DFA32_transition;

    static {
        int numStates = DFA32_transitionS.length;
        DFA32_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA32_transition[i] = DFA.unpackEncodedString(DFA32_transitionS[i]);
        }
    }

    class DFA32 extends DFA {

        public DFA32(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 32;
            this.eot = DFA32_eot;
            this.eof = DFA32_eof;
            this.min = DFA32_min;
            this.max = DFA32_max;
            this.accept = DFA32_accept;
            this.special = DFA32_special;
            this.transition = DFA32_transition;
        }
        public String getDescription() {
            return "()* loopback of 292:8: ( interfaceBodyDeclaration )*";
        }
    }
    static final String DFA35_eotS =
        "\52\uffff";
    static final String DFA35_eofS =
        "\52\uffff";
    static final String DFA35_minS =
        "\1\4\1\uffff\1\4\47\uffff";
    static final String DFA35_maxS =
        "\1\107\1\uffff\1\107\47\uffff";
    static final String DFA35_acceptS =
        "\1\uffff\1\1\1\uffff\1\2\1\3\45\uffff";
    static final String DFA35_specialS =
        "\52\uffff}>";
    static final String[] DFA35_transitionS = {
            "\2\4\23\uffff\1\1\1\uffff\1\2\2\uffff\1\4\2\uffff\1\4\3\uffff"+
            "\1\3\1\uffff\2\4\4\uffff\22\4\10\uffff\1\4",
            "",
            "\2\4\25\uffff\1\4\2\uffff\1\4\2\uffff\1\4\3\uffff\1\3\1\uffff"+
            "\2\4\4\uffff\22\4\10\uffff\1\4",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA35_eot = DFA.unpackEncodedString(DFA35_eotS);
    static final short[] DFA35_eof = DFA.unpackEncodedString(DFA35_eofS);
    static final char[] DFA35_min = DFA.unpackEncodedStringToUnsignedChars(DFA35_minS);
    static final char[] DFA35_max = DFA.unpackEncodedStringToUnsignedChars(DFA35_maxS);
    static final short[] DFA35_accept = DFA.unpackEncodedString(DFA35_acceptS);
    static final short[] DFA35_special = DFA.unpackEncodedString(DFA35_specialS);
    static final short[][] DFA35_transition;

    static {
        int numStates = DFA35_transitionS.length;
        DFA35_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA35_transition[i] = DFA.unpackEncodedString(DFA35_transitionS[i]);
        }
    }

    class DFA35 extends DFA {

        public DFA35(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 35;
            this.eot = DFA35_eot;
            this.eof = DFA35_eof;
            this.min = DFA35_min;
            this.max = DFA35_max;
            this.accept = DFA35_accept;
            this.special = DFA35_special;
            this.transition = DFA35_transition;
        }
        public String getDescription() {
            return "295:1: classBodyDeclaration : ( ';' | ( 'static' )? block | ( modifier )* memberDecl );";
        }
    }
    static final String DFA34_eotS =
        "\26\uffff";
    static final String DFA34_eofS =
        "\26\uffff";
    static final String DFA34_minS =
        "\1\4\5\uffff\1\4\17\uffff";
    static final String DFA34_maxS =
        "\1\107\5\uffff\1\47\17\uffff";
    static final String DFA34_acceptS =
        "\1\uffff\1\2\7\uffff\1\1\14\uffff";
    static final String DFA34_specialS =
        "\26\uffff}>";
    static final String[] DFA34_transitionS = {
            "\2\1\25\uffff\1\11\2\uffff\1\1\2\uffff\1\1\5\uffff\2\1\4\uffff"+
            "\12\11\10\1\10\uffff\1\6",
            "",
            "",
            "",
            "",
            "",
            "\1\11\42\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA34_eot = DFA.unpackEncodedString(DFA34_eotS);
    static final short[] DFA34_eof = DFA.unpackEncodedString(DFA34_eofS);
    static final char[] DFA34_min = DFA.unpackEncodedStringToUnsignedChars(DFA34_minS);
    static final char[] DFA34_max = DFA.unpackEncodedStringToUnsignedChars(DFA34_maxS);
    static final short[] DFA34_accept = DFA.unpackEncodedString(DFA34_acceptS);
    static final short[] DFA34_special = DFA.unpackEncodedString(DFA34_specialS);
    static final short[][] DFA34_transition;

    static {
        int numStates = DFA34_transitionS.length;
        DFA34_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA34_transition[i] = DFA.unpackEncodedString(DFA34_transitionS[i]);
        }
    }

    class DFA34 extends DFA {

        public DFA34(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 34;
            this.eot = DFA34_eot;
            this.eof = DFA34_eof;
            this.min = DFA34_min;
            this.max = DFA34_max;
            this.accept = DFA34_accept;
            this.special = DFA34_special;
            this.transition = DFA34_transition;
        }
        public String getDescription() {
            return "()* loopback of 298:4: ( modifier )*";
        }
    }
    static final String DFA36_eotS =
        "\22\uffff";
    static final String DFA36_eofS =
        "\22\uffff";
    static final String DFA36_minS =
        "\1\4\1\uffff\2\4\5\uffff\4\0\1\uffff\2\0\2\uffff";
    static final String DFA36_maxS =
        "\1\107\1\uffff\1\101\1\51\5\uffff\4\0\1\uffff\2\0\2\uffff";
    static final String DFA36_acceptS =
        "\1\uffff\1\1\2\uffff\1\4\1\6\1\uffff\1\7\5\uffff\1\5\2\uffff\1"+
        "\2\1\3";
    static final String DFA36_specialS =
        "\11\uffff\1\0\1\1\1\2\1\3\1\uffff\1\4\1\5\2\uffff}>";
    static final String[] DFA36_transitionS = {
            "\1\2\1\7\30\uffff\1\7\2\uffff\1\1\5\uffff\1\5\1\4\16\uffff"+
            "\10\3\10\uffff\1\5",
            "",
            "\1\14\27\uffff\1\12\4\uffff\1\11\7\uffff\1\13\27\uffff\1\15",
            "\1\17\44\uffff\1\16",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA36_eot = DFA.unpackEncodedString(DFA36_eotS);
    static final short[] DFA36_eof = DFA.unpackEncodedString(DFA36_eofS);
    static final char[] DFA36_min = DFA.unpackEncodedStringToUnsignedChars(DFA36_minS);
    static final char[] DFA36_max = DFA.unpackEncodedStringToUnsignedChars(DFA36_maxS);
    static final short[] DFA36_accept = DFA.unpackEncodedString(DFA36_acceptS);
    static final short[] DFA36_special = DFA.unpackEncodedString(DFA36_specialS);
    static final short[][] DFA36_transition;

    static {
        int numStates = DFA36_transitionS.length;
        DFA36_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA36_transition[i] = DFA.unpackEncodedString(DFA36_transitionS[i]);
        }
    }

    class DFA36 extends DFA {

        public DFA36(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 36;
            this.eot = DFA36_eot;
            this.eof = DFA36_eof;
            this.min = DFA36_min;
            this.max = DFA36_max;
            this.accept = DFA36_accept;
            this.special = DFA36_special;
            this.transition = DFA36_transition;
        }
        public String getDescription() {
            return "301:1: memberDecl : ( genericMethodOrConstructorDecl | methodDeclaration | fieldDeclaration | 'void' Identifier voidMethodDeclaratorRest | Identifier constructorDeclaratorRest | interfaceDeclaration | classDeclaration );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA36_9 = input.LA(1);

                         
                        int index36_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred38_Java()) ) {s = 16;}

                        else if ( (synpred39_Java()) ) {s = 17;}

                         
                        input.seek(index36_9);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA36_10 = input.LA(1);

                         
                        int index36_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred38_Java()) ) {s = 16;}

                        else if ( (synpred39_Java()) ) {s = 17;}

                         
                        input.seek(index36_10);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA36_11 = input.LA(1);

                         
                        int index36_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred38_Java()) ) {s = 16;}

                        else if ( (synpred39_Java()) ) {s = 17;}

                         
                        input.seek(index36_11);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA36_12 = input.LA(1);

                         
                        int index36_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred38_Java()) ) {s = 16;}

                        else if ( (synpred39_Java()) ) {s = 17;}

                         
                        input.seek(index36_12);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA36_14 = input.LA(1);

                         
                        int index36_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred38_Java()) ) {s = 16;}

                        else if ( (synpred39_Java()) ) {s = 17;}

                         
                        input.seek(index36_14);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA36_15 = input.LA(1);

                         
                        int index36_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred38_Java()) ) {s = 16;}

                        else if ( (synpred39_Java()) ) {s = 17;}

                         
                        input.seek(index36_15);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 36, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA40_eotS =
        "\25\uffff";
    static final String DFA40_eofS =
        "\25\uffff";
    static final String DFA40_minS =
        "\1\4\24\uffff";
    static final String DFA40_maxS =
        "\1\107\24\uffff";
    static final String DFA40_acceptS =
        "\1\uffff\1\1\22\uffff\1\2";
    static final String DFA40_specialS =
        "\25\uffff}>";
    static final String[] DFA40_transitionS = {
            "\2\1\23\uffff\1\24\1\uffff\1\1\2\uffff\1\1\2\uffff\1\1\5\uffff"+
            "\2\1\4\uffff\22\1\10\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA40_eot = DFA.unpackEncodedString(DFA40_eotS);
    static final short[] DFA40_eof = DFA.unpackEncodedString(DFA40_eofS);
    static final char[] DFA40_min = DFA.unpackEncodedStringToUnsignedChars(DFA40_minS);
    static final char[] DFA40_max = DFA.unpackEncodedStringToUnsignedChars(DFA40_maxS);
    static final short[] DFA40_accept = DFA.unpackEncodedString(DFA40_acceptS);
    static final short[] DFA40_special = DFA.unpackEncodedString(DFA40_specialS);
    static final short[][] DFA40_transition;

    static {
        int numStates = DFA40_transitionS.length;
        DFA40_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA40_transition[i] = DFA.unpackEncodedString(DFA40_transitionS[i]);
        }
    }

    class DFA40 extends DFA {

        public DFA40(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 40;
            this.eot = DFA40_eot;
            this.eof = DFA40_eof;
            this.min = DFA40_min;
            this.max = DFA40_max;
            this.accept = DFA40_accept;
            this.special = DFA40_special;
            this.transition = DFA40_transition;
        }
        public String getDescription() {
            return "328:1: interfaceBodyDeclaration : ( ( modifier )* interfaceMemberDecl | ';' );";
        }
    }
    static final String DFA39_eotS =
        "\26\uffff";
    static final String DFA39_eofS =
        "\26\uffff";
    static final String DFA39_minS =
        "\1\4\5\uffff\1\4\17\uffff";
    static final String DFA39_maxS =
        "\1\107\5\uffff\1\47\17\uffff";
    static final String DFA39_acceptS =
        "\1\uffff\1\2\7\uffff\1\1\14\uffff";
    static final String DFA39_specialS =
        "\26\uffff}>";
    static final String[] DFA39_transitionS = {
            "\2\1\25\uffff\1\11\2\uffff\1\1\2\uffff\1\1\5\uffff\2\1\4\uffff"+
            "\12\11\10\1\10\uffff\1\6",
            "",
            "",
            "",
            "",
            "",
            "\1\11\42\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA39_eot = DFA.unpackEncodedString(DFA39_eotS);
    static final short[] DFA39_eof = DFA.unpackEncodedString(DFA39_eofS);
    static final char[] DFA39_min = DFA.unpackEncodedStringToUnsignedChars(DFA39_minS);
    static final char[] DFA39_max = DFA.unpackEncodedStringToUnsignedChars(DFA39_maxS);
    static final short[] DFA39_accept = DFA.unpackEncodedString(DFA39_acceptS);
    static final short[] DFA39_special = DFA.unpackEncodedString(DFA39_specialS);
    static final short[][] DFA39_transition;

    static {
        int numStates = DFA39_transitionS.length;
        DFA39_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA39_transition[i] = DFA.unpackEncodedString(DFA39_transitionS[i]);
        }
    }

    class DFA39 extends DFA {

        public DFA39(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 39;
            this.eot = DFA39_eot;
            this.eof = DFA39_eof;
            this.min = DFA39_min;
            this.max = DFA39_max;
            this.accept = DFA39_accept;
            this.special = DFA39_special;
            this.transition = DFA39_transition;
        }
        public String getDescription() {
            return "()* loopback of 329:4: ( modifier )*";
        }
    }
    static final String DFA60_eotS =
        "\31\uffff";
    static final String DFA60_eofS =
        "\31\uffff";
    static final String DFA60_minS =
        "\1\4\30\uffff";
    static final String DFA60_maxS =
        "\1\167\30\uffff";
    static final String DFA60_acceptS =
        "\1\uffff\1\1\1\2\26\uffff";
    static final String DFA60_specialS =
        "\31\uffff}>";
    static final String[] DFA60_transitionS = {
            "\1\2\1\uffff\6\2\25\uffff\1\2\3\uffff\1\1\2\uffff\1\2\16\uffff"+
            "\10\2\1\uffff\2\2\2\uffff\3\2\23\uffff\3\2\21\uffff\2\2\2\uffff"+
            "\6\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA60_eot = DFA.unpackEncodedString(DFA60_eotS);
    static final short[] DFA60_eof = DFA.unpackEncodedString(DFA60_eofS);
    static final char[] DFA60_min = DFA.unpackEncodedStringToUnsignedChars(DFA60_minS);
    static final char[] DFA60_max = DFA.unpackEncodedStringToUnsignedChars(DFA60_maxS);
    static final short[] DFA60_accept = DFA.unpackEncodedString(DFA60_acceptS);
    static final short[] DFA60_special = DFA.unpackEncodedString(DFA60_specialS);
    static final short[][] DFA60_transition;

    static {
        int numStates = DFA60_transitionS.length;
        DFA60_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA60_transition[i] = DFA.unpackEncodedString(DFA60_transitionS[i]);
        }
    }

    class DFA60 extends DFA {

        public DFA60(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 60;
            this.eot = DFA60_eot;
            this.eof = DFA60_eof;
            this.min = DFA60_min;
            this.max = DFA60_max;
            this.accept = DFA60_accept;
            this.special = DFA60_special;
            this.transition = DFA60_transition;
        }
        public String getDescription() {
            return "434:1: variableInitializer : ( arrayInitializer | expression );";
        }
    }
    static final String DFA63_eotS =
        "\32\uffff";
    static final String DFA63_eofS =
        "\32\uffff";
    static final String DFA63_minS =
        "\1\4\31\uffff";
    static final String DFA63_maxS =
        "\1\167\31\uffff";
    static final String DFA63_acceptS =
        "\1\uffff\1\1\27\uffff\1\2";
    static final String DFA63_specialS =
        "\32\uffff}>";
    static final String[] DFA63_transitionS = {
            "\1\1\1\uffff\6\1\25\uffff\1\1\3\uffff\1\1\1\31\1\uffff\1\1"+
            "\16\uffff\10\1\1\uffff\2\1\2\uffff\3\1\23\uffff\3\1\21\uffff"+
            "\2\1\2\uffff\6\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA63_eot = DFA.unpackEncodedString(DFA63_eotS);
    static final short[] DFA63_eof = DFA.unpackEncodedString(DFA63_eofS);
    static final char[] DFA63_min = DFA.unpackEncodedStringToUnsignedChars(DFA63_minS);
    static final char[] DFA63_max = DFA.unpackEncodedStringToUnsignedChars(DFA63_maxS);
    static final short[] DFA63_accept = DFA.unpackEncodedString(DFA63_acceptS);
    static final short[] DFA63_special = DFA.unpackEncodedString(DFA63_specialS);
    static final short[][] DFA63_transition;

    static {
        int numStates = DFA63_transitionS.length;
        DFA63_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA63_transition[i] = DFA.unpackEncodedString(DFA63_transitionS[i]);
        }
    }

    class DFA63 extends DFA {

        public DFA63(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 63;
            this.eot = DFA63_eot;
            this.eof = DFA63_eof;
            this.min = DFA63_min;
            this.max = DFA63_max;
            this.accept = DFA63_accept;
            this.special = DFA63_special;
            this.transition = DFA63_transition;
        }
        public String getDescription() {
            return "440:8: ( variableInitializer ( ',' variableInitializer )* ( ',' )? )?";
        }
    }
    static final String DFA61_eotS =
        "\34\uffff";
    static final String DFA61_eofS =
        "\34\uffff";
    static final String DFA61_minS =
        "\1\42\1\4\32\uffff";
    static final String DFA61_maxS =
        "\1\46\1\167\32\uffff";
    static final String DFA61_acceptS =
        "\2\uffff\1\2\1\uffff\1\1\27\uffff";
    static final String DFA61_specialS =
        "\34\uffff}>";
    static final String[] DFA61_transitionS = {
            "\1\1\3\uffff\1\2",
            "\1\4\1\uffff\6\4\25\uffff\1\4\3\uffff\1\4\1\2\1\uffff\1\4"+
            "\16\uffff\10\4\1\uffff\2\4\2\uffff\3\4\23\uffff\3\4\21\uffff"+
            "\2\4\2\uffff\6\4",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA61_eot = DFA.unpackEncodedString(DFA61_eotS);
    static final short[] DFA61_eof = DFA.unpackEncodedString(DFA61_eofS);
    static final char[] DFA61_min = DFA.unpackEncodedStringToUnsignedChars(DFA61_minS);
    static final char[] DFA61_max = DFA.unpackEncodedStringToUnsignedChars(DFA61_maxS);
    static final short[] DFA61_accept = DFA.unpackEncodedString(DFA61_acceptS);
    static final short[] DFA61_special = DFA.unpackEncodedString(DFA61_specialS);
    static final short[][] DFA61_transition;

    static {
        int numStates = DFA61_transitionS.length;
        DFA61_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA61_transition[i] = DFA.unpackEncodedString(DFA61_transitionS[i]);
        }
    }

    class DFA61 extends DFA {

        public DFA61(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 61;
            this.eot = DFA61_eot;
            this.eof = DFA61_eof;
            this.min = DFA61_min;
            this.max = DFA61_max;
            this.accept = DFA61_accept;
            this.special = DFA61_special;
            this.transition = DFA61_transition;
        }
        public String getDescription() {
            return "()* loopback of 440:29: ( ',' variableInitializer )*";
        }
    }
    static final String DFA64_eotS =
        "\15\uffff";
    static final String DFA64_eofS =
        "\15\uffff";
    static final String DFA64_minS =
        "\1\33\14\uffff";
    static final String DFA64_maxS =
        "\1\107\14\uffff";
    static final String DFA64_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14";
    static final String DFA64_specialS =
        "\15\uffff}>";
    static final String[] DFA64_transitionS = {
            "\1\5\21\uffff\1\2\1\3\1\4\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
            "\20\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA64_eot = DFA.unpackEncodedString(DFA64_eotS);
    static final short[] DFA64_eof = DFA.unpackEncodedString(DFA64_eofS);
    static final char[] DFA64_min = DFA.unpackEncodedStringToUnsignedChars(DFA64_minS);
    static final char[] DFA64_max = DFA.unpackEncodedStringToUnsignedChars(DFA64_maxS);
    static final short[] DFA64_accept = DFA.unpackEncodedString(DFA64_acceptS);
    static final short[] DFA64_special = DFA.unpackEncodedString(DFA64_specialS);
    static final short[][] DFA64_transition;

    static {
        int numStates = DFA64_transitionS.length;
        DFA64_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA64_transition[i] = DFA.unpackEncodedString(DFA64_transitionS[i]);
        }
    }

    class DFA64 extends DFA {

        public DFA64(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 64;
            this.eot = DFA64_eot;
            this.eof = DFA64_eof;
            this.min = DFA64_min;
            this.max = DFA64_max;
            this.accept = DFA64_accept;
            this.special = DFA64_special;
            this.transition = DFA64_transition;
        }
        public String getDescription() {
            return "443:1: modifier : ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' );";
        }
    }
    static final String DFA67_eotS =
        "\44\uffff";
    static final String DFA67_eofS =
        "\1\2\43\uffff";
    static final String DFA67_minS =
        "\2\4\42\uffff";
    static final String DFA67_maxS =
        "\1\154\1\77\42\uffff";
    static final String DFA67_acceptS =
        "\2\uffff\1\2\36\uffff\1\1\2\uffff";
    static final String DFA67_specialS =
        "\44\uffff}>";
    static final String[] DFA67_transitionS = {
            "\1\2\24\uffff\1\2\2\uffff\1\2\3\uffff\1\2\1\1\5\2\2\uffff\2"+
            "\2\1\uffff\1\2\22\uffff\1\2\2\uffff\2\2\6\uffff\1\2\24\uffff"+
            "\16\2",
            "\1\41\34\uffff\1\2\25\uffff\11\41",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA67_eot = DFA.unpackEncodedString(DFA67_eotS);
    static final short[] DFA67_eof = DFA.unpackEncodedString(DFA67_eofS);
    static final char[] DFA67_min = DFA.unpackEncodedStringToUnsignedChars(DFA67_minS);
    static final char[] DFA67_max = DFA.unpackEncodedStringToUnsignedChars(DFA67_maxS);
    static final short[] DFA67_accept = DFA.unpackEncodedString(DFA67_acceptS);
    static final short[] DFA67_special = DFA.unpackEncodedString(DFA67_specialS);
    static final short[][] DFA67_transition;

    static {
        int numStates = DFA67_transitionS.length;
        DFA67_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA67_transition[i] = DFA.unpackEncodedString(DFA67_transitionS[i]);
        }
    }

    class DFA67 extends DFA {

        public DFA67(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 67;
            this.eot = DFA67_eot;
            this.eof = DFA67_eof;
            this.min = DFA67_min;
            this.max = DFA67_max;
            this.accept = DFA67_accept;
            this.special = DFA67_special;
            this.transition = DFA67_transition;
        }
        public String getDescription() {
            return "472:15: ( typeArguments )?";
        }
    }
    static final String DFA69_eotS =
        "\40\uffff";
    static final String DFA69_eofS =
        "\1\1\37\uffff";
    static final String DFA69_minS =
        "\1\4\37\uffff";
    static final String DFA69_maxS =
        "\1\154\37\uffff";
    static final String DFA69_acceptS =
        "\1\uffff\1\2\35\uffff\1\1";
    static final String DFA69_specialS =
        "\40\uffff}>";
    static final String[] DFA69_transitionS = {
            "\1\1\24\uffff\1\1\2\uffff\1\37\3\uffff\7\1\2\uffff\2\1\1\uffff"+
            "\1\1\22\uffff\1\1\2\uffff\2\1\6\uffff\1\1\24\uffff\16\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA69_eot = DFA.unpackEncodedString(DFA69_eotS);
    static final short[] DFA69_eof = DFA.unpackEncodedString(DFA69_eofS);
    static final char[] DFA69_min = DFA.unpackEncodedStringToUnsignedChars(DFA69_minS);
    static final char[] DFA69_max = DFA.unpackEncodedStringToUnsignedChars(DFA69_maxS);
    static final short[] DFA69_accept = DFA.unpackEncodedString(DFA69_acceptS);
    static final short[] DFA69_special = DFA.unpackEncodedString(DFA69_specialS);
    static final short[][] DFA69_transition;

    static {
        int numStates = DFA69_transitionS.length;
        DFA69_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA69_transition[i] = DFA.unpackEncodedString(DFA69_transitionS[i]);
        }
    }

    class DFA69 extends DFA {

        public DFA69(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 69;
            this.eot = DFA69_eot;
            this.eof = DFA69_eof;
            this.min = DFA69_min;
            this.max = DFA69_max;
            this.accept = DFA69_accept;
            this.special = DFA69_special;
            this.transition = DFA69_transition;
        }
        public String getDescription() {
            return "()* loopback of 472:32: ( '.' Identifier ( typeArguments )? )*";
        }
    }
    static final String DFA68_eotS =
        "\44\uffff";
    static final String DFA68_eofS =
        "\1\2\43\uffff";
    static final String DFA68_minS =
        "\2\4\42\uffff";
    static final String DFA68_maxS =
        "\1\154\1\77\42\uffff";
    static final String DFA68_acceptS =
        "\2\uffff\1\2\36\uffff\1\1\2\uffff";
    static final String DFA68_specialS =
        "\44\uffff}>";
    static final String[] DFA68_transitionS = {
            "\1\2\24\uffff\1\2\2\uffff\1\2\3\uffff\1\2\1\1\5\2\2\uffff\2"+
            "\2\1\uffff\1\2\22\uffff\1\2\2\uffff\2\2\6\uffff\1\2\24\uffff"+
            "\16\2",
            "\1\41\34\uffff\1\2\25\uffff\11\41",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA68_eot = DFA.unpackEncodedString(DFA68_eotS);
    static final short[] DFA68_eof = DFA.unpackEncodedString(DFA68_eofS);
    static final char[] DFA68_min = DFA.unpackEncodedStringToUnsignedChars(DFA68_minS);
    static final char[] DFA68_max = DFA.unpackEncodedStringToUnsignedChars(DFA68_maxS);
    static final short[] DFA68_accept = DFA.unpackEncodedString(DFA68_acceptS);
    static final short[] DFA68_special = DFA.unpackEncodedString(DFA68_specialS);
    static final short[][] DFA68_transition;

    static {
        int numStates = DFA68_transitionS.length;
        DFA68_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA68_transition[i] = DFA.unpackEncodedString(DFA68_transitionS[i]);
        }
    }

    class DFA68 extends DFA {

        public DFA68(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 68;
            this.eot = DFA68_eot;
            this.eof = DFA68_eof;
            this.min = DFA68_min;
            this.max = DFA68_max;
            this.accept = DFA68_accept;
            this.special = DFA68_special;
            this.transition = DFA68_transition;
        }
        public String getDescription() {
            return "472:48: ( typeArguments )?";
        }
    }
    static final String DFA70_eotS =
        "\37\uffff";
    static final String DFA70_eofS =
        "\1\1\36\uffff";
    static final String DFA70_minS =
        "\1\4\36\uffff";
    static final String DFA70_maxS =
        "\1\154\36\uffff";
    static final String DFA70_acceptS =
        "\1\uffff\1\2\34\uffff\1\1";
    static final String DFA70_specialS =
        "\37\uffff}>";
    static final String[] DFA70_transitionS = {
            "\1\1\24\uffff\1\1\6\uffff\7\1\2\uffff\1\36\1\1\1\uffff\1\1"+
            "\22\uffff\1\1\2\uffff\2\1\6\uffff\1\1\24\uffff\16\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA70_eot = DFA.unpackEncodedString(DFA70_eotS);
    static final short[] DFA70_eof = DFA.unpackEncodedString(DFA70_eofS);
    static final char[] DFA70_min = DFA.unpackEncodedStringToUnsignedChars(DFA70_minS);
    static final char[] DFA70_max = DFA.unpackEncodedStringToUnsignedChars(DFA70_maxS);
    static final short[] DFA70_accept = DFA.unpackEncodedString(DFA70_acceptS);
    static final short[] DFA70_special = DFA.unpackEncodedString(DFA70_specialS);
    static final short[][] DFA70_transition;

    static {
        int numStates = DFA70_transitionS.length;
        DFA70_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA70_transition[i] = DFA.unpackEncodedString(DFA70_transitionS[i]);
        }
    }

    class DFA70 extends DFA {

        public DFA70(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 70;
            this.eot = DFA70_eot;
            this.eof = DFA70_eof;
            this.min = DFA70_min;
            this.max = DFA70_max;
            this.accept = DFA70_accept;
            this.special = DFA70_special;
            this.transition = DFA70_transition;
        }
        public String getDescription() {
            return "()* loopback of 472:68: ( '[' ']' )*";
        }
    }
    static final String DFA71_eotS =
        "\37\uffff";
    static final String DFA71_eofS =
        "\1\1\36\uffff";
    static final String DFA71_minS =
        "\1\4\36\uffff";
    static final String DFA71_maxS =
        "\1\154\36\uffff";
    static final String DFA71_acceptS =
        "\1\uffff\1\2\34\uffff\1\1";
    static final String DFA71_specialS =
        "\37\uffff}>";
    static final String[] DFA71_transitionS = {
            "\1\1\24\uffff\1\1\6\uffff\7\1\2\uffff\1\36\1\1\1\uffff\1\1"+
            "\22\uffff\1\1\2\uffff\2\1\6\uffff\1\1\24\uffff\16\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA71_eot = DFA.unpackEncodedString(DFA71_eotS);
    static final short[] DFA71_eof = DFA.unpackEncodedString(DFA71_eofS);
    static final char[] DFA71_min = DFA.unpackEncodedStringToUnsignedChars(DFA71_minS);
    static final char[] DFA71_max = DFA.unpackEncodedStringToUnsignedChars(DFA71_maxS);
    static final short[] DFA71_accept = DFA.unpackEncodedString(DFA71_acceptS);
    static final short[] DFA71_special = DFA.unpackEncodedString(DFA71_specialS);
    static final short[][] DFA71_transition;

    static {
        int numStates = DFA71_transitionS.length;
        DFA71_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA71_transition[i] = DFA.unpackEncodedString(DFA71_transitionS[i]);
        }
    }

    class DFA71 extends DFA {

        public DFA71(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 71;
            this.eot = DFA71_eot;
            this.eof = DFA71_eof;
            this.min = DFA71_min;
            this.max = DFA71_max;
            this.accept = DFA71_accept;
            this.special = DFA71_special;
            this.transition = DFA71_transition;
        }
        public String getDescription() {
            return "()* loopback of 473:18: ( '[' ']' )*";
        }
    }
    static final String DFA85_eotS =
        "\30\uffff";
    static final String DFA85_eofS =
        "\1\1\27\uffff";
    static final String DFA85_minS =
        "\1\4\2\uffff\1\4\22\uffff\1\0\1\uffff";
    static final String DFA85_maxS =
        "\1\107\2\uffff\1\47\22\uffff\1\0\1\uffff";
    static final String DFA85_acceptS =
        "\1\uffff\1\2\25\uffff\1\1";
    static final String DFA85_specialS =
        "\26\uffff\1\0\1\uffff}>";
    static final String[] DFA85_transitionS = {
            "\2\1\22\uffff\4\1\2\uffff\1\1\10\uffff\1\1\5\uffff\12\1\20"+
            "\uffff\1\3",
            "",
            "",
            "\1\26\42\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            ""
    };

    static final short[] DFA85_eot = DFA.unpackEncodedString(DFA85_eotS);
    static final short[] DFA85_eof = DFA.unpackEncodedString(DFA85_eofS);
    static final char[] DFA85_min = DFA.unpackEncodedStringToUnsignedChars(DFA85_minS);
    static final char[] DFA85_max = DFA.unpackEncodedStringToUnsignedChars(DFA85_maxS);
    static final short[] DFA85_accept = DFA.unpackEncodedString(DFA85_acceptS);
    static final short[] DFA85_special = DFA.unpackEncodedString(DFA85_specialS);
    static final short[][] DFA85_transition;

    static {
        int numStates = DFA85_transitionS.length;
        DFA85_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA85_transition[i] = DFA.unpackEncodedString(DFA85_transitionS[i]);
        }
    }

    class DFA85 extends DFA {

        public DFA85(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 85;
            this.eot = DFA85_eot;
            this.eof = DFA85_eof;
            this.min = DFA85_min;
            this.max = DFA85_max;
            this.accept = DFA85_accept;
            this.special = DFA85_special;
            this.transition = DFA85_transition;
        }
        public String getDescription() {
            return "()+ loopback of 549:4: ( annotation )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA85_22 = input.LA(1);

                         
                        int index85_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_Java()) ) {s = 23;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index85_22);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 85, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA87_eotS =
        "\34\uffff";
    static final String DFA87_eofS =
        "\1\2\33\uffff";
    static final String DFA87_minS =
        "\1\4\33\uffff";
    static final String DFA87_maxS =
        "\1\107\33\uffff";
    static final String DFA87_acceptS =
        "\1\uffff\1\1\1\2\31\uffff";
    static final String DFA87_specialS =
        "\34\uffff}>";
    static final String[] DFA87_transitionS = {
            "\2\2\22\uffff\4\2\2\uffff\1\2\2\uffff\2\2\3\uffff\3\2\4\uffff"+
            "\22\2\2\uffff\1\1\1\2\4\uffff\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA87_eot = DFA.unpackEncodedString(DFA87_eotS);
    static final short[] DFA87_eof = DFA.unpackEncodedString(DFA87_eofS);
    static final char[] DFA87_min = DFA.unpackEncodedStringToUnsignedChars(DFA87_minS);
    static final char[] DFA87_max = DFA.unpackEncodedStringToUnsignedChars(DFA87_maxS);
    static final short[] DFA87_accept = DFA.unpackEncodedString(DFA87_acceptS);
    static final short[] DFA87_special = DFA.unpackEncodedString(DFA87_specialS);
    static final short[][] DFA87_transition;

    static {
        int numStates = DFA87_transitionS.length;
        DFA87_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA87_transition[i] = DFA.unpackEncodedString(DFA87_transitionS[i]);
        }
    }

    class DFA87 extends DFA {

        public DFA87(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 87;
            this.eot = DFA87_eot;
            this.eof = DFA87_eof;
            this.min = DFA87_min;
            this.max = DFA87_max;
            this.accept = DFA87_accept;
            this.special = DFA87_special;
            this.transition = DFA87_transition;
        }
        public String getDescription() {
            return "553:23: ( '(' ( elementValuePairs )? ')' )?";
        }
    }
    static final String DFA86_eotS =
        "\33\uffff";
    static final String DFA86_eofS =
        "\33\uffff";
    static final String DFA86_minS =
        "\1\4\32\uffff";
    static final String DFA86_maxS =
        "\1\167\32\uffff";
    static final String DFA86_acceptS =
        "\1\uffff\1\1\30\uffff\1\2";
    static final String DFA86_specialS =
        "\33\uffff}>";
    static final String[] DFA86_transitionS = {
            "\1\1\1\uffff\6\1\25\uffff\1\1\3\uffff\1\1\2\uffff\1\1\16\uffff"+
            "\10\1\1\uffff\2\1\1\32\1\uffff\4\1\22\uffff\3\1\21\uffff\2\1"+
            "\2\uffff\6\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA86_eot = DFA.unpackEncodedString(DFA86_eotS);
    static final short[] DFA86_eof = DFA.unpackEncodedString(DFA86_eofS);
    static final char[] DFA86_min = DFA.unpackEncodedStringToUnsignedChars(DFA86_minS);
    static final char[] DFA86_max = DFA.unpackEncodedStringToUnsignedChars(DFA86_maxS);
    static final short[] DFA86_accept = DFA.unpackEncodedString(DFA86_acceptS);
    static final short[] DFA86_special = DFA.unpackEncodedString(DFA86_specialS);
    static final short[][] DFA86_transition;

    static {
        int numStates = DFA86_transitionS.length;
        DFA86_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA86_transition[i] = DFA.unpackEncodedString(DFA86_transitionS[i]);
        }
    }

    class DFA86 extends DFA {

        public DFA86(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 86;
            this.eot = DFA86_eot;
            this.eof = DFA86_eof;
            this.min = DFA86_min;
            this.max = DFA86_max;
            this.accept = DFA86_accept;
            this.special = DFA86_special;
            this.transition = DFA86_transition;
        }
        public String getDescription() {
            return "553:28: ( elementValuePairs )?";
        }
    }
    static final String DFA88_eotS =
        "\35\uffff";
    static final String DFA88_eofS =
        "\1\1\34\uffff";
    static final String DFA88_minS =
        "\1\4\34\uffff";
    static final String DFA88_maxS =
        "\1\107\34\uffff";
    static final String DFA88_acceptS =
        "\1\uffff\1\2\32\uffff\1\1";
    static final String DFA88_specialS =
        "\35\uffff}>";
    static final String[] DFA88_transitionS = {
            "\2\1\22\uffff\4\1\1\34\1\uffff\1\1\2\uffff\2\1\3\uffff\3\1"+
            "\4\uffff\22\1\2\uffff\2\1\4\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA88_eot = DFA.unpackEncodedString(DFA88_eotS);
    static final short[] DFA88_eof = DFA.unpackEncodedString(DFA88_eofS);
    static final char[] DFA88_min = DFA.unpackEncodedStringToUnsignedChars(DFA88_minS);
    static final char[] DFA88_max = DFA.unpackEncodedStringToUnsignedChars(DFA88_maxS);
    static final short[] DFA88_accept = DFA.unpackEncodedString(DFA88_acceptS);
    static final short[] DFA88_special = DFA.unpackEncodedString(DFA88_specialS);
    static final short[][] DFA88_transition;

    static {
        int numStates = DFA88_transitionS.length;
        DFA88_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA88_transition[i] = DFA.unpackEncodedString(DFA88_transitionS[i]);
        }
    }

    class DFA88 extends DFA {

        public DFA88(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 88;
            this.eot = DFA88_eot;
            this.eof = DFA88_eof;
            this.min = DFA88_min;
            this.max = DFA88_max;
            this.accept = DFA88_accept;
            this.special = DFA88_special;
            this.transition = DFA88_transition;
        }
        public String getDescription() {
            return "()* loopback of 557:15: ( '.' Identifier )*";
        }
    }
    static final String DFA90_eotS =
        "\56\uffff";
    static final String DFA90_eofS =
        "\1\uffff\1\2\54\uffff";
    static final String DFA90_minS =
        "\1\4\1\34\54\uffff";
    static final String DFA90_maxS =
        "\1\167\1\163\54\uffff";
    static final String DFA90_acceptS =
        "\2\uffff\1\2\27\uffff\1\1\23\uffff";
    static final String DFA90_specialS =
        "\56\uffff}>";
    static final String[] DFA90_transitionS = {
            "\1\1\1\uffff\6\2\25\uffff\1\2\3\uffff\1\2\2\uffff\1\2\16\uffff"+
            "\10\2\1\uffff\2\2\2\uffff\4\2\22\uffff\3\2\21\uffff\2\2\2\uffff"+
            "\6\2",
            "\2\2\3\uffff\4\2\4\uffff\1\2\2\uffff\1\32\22\uffff\1\2\1\uffff"+
            "\2\2\44\uffff\15\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA90_eot = DFA.unpackEncodedString(DFA90_eotS);
    static final short[] DFA90_eof = DFA.unpackEncodedString(DFA90_eofS);
    static final char[] DFA90_min = DFA.unpackEncodedStringToUnsignedChars(DFA90_minS);
    static final char[] DFA90_max = DFA.unpackEncodedStringToUnsignedChars(DFA90_maxS);
    static final short[] DFA90_accept = DFA.unpackEncodedString(DFA90_acceptS);
    static final short[] DFA90_special = DFA.unpackEncodedString(DFA90_specialS);
    static final short[][] DFA90_transition;

    static {
        int numStates = DFA90_transitionS.length;
        DFA90_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA90_transition[i] = DFA.unpackEncodedString(DFA90_transitionS[i]);
        }
    }

    class DFA90 extends DFA {

        public DFA90(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 90;
            this.eot = DFA90_eot;
            this.eof = DFA90_eof;
            this.min = DFA90_min;
            this.max = DFA90_max;
            this.accept = DFA90_accept;
            this.special = DFA90_special;
            this.transition = DFA90_transition;
        }
        public String getDescription() {
            return "565:4: ( Identifier '=' )?";
        }
    }
    static final String DFA91_eotS =
        "\32\uffff";
    static final String DFA91_eofS =
        "\32\uffff";
    static final String DFA91_minS =
        "\1\4\31\uffff";
    static final String DFA91_maxS =
        "\1\167\31\uffff";
    static final String DFA91_acceptS =
        "\1\uffff\1\1\26\uffff\1\2\1\3";
    static final String DFA91_specialS =
        "\32\uffff}>";
    static final String[] DFA91_transitionS = {
            "\1\1\1\uffff\6\1\25\uffff\1\1\3\uffff\1\31\2\uffff\1\1\16\uffff"+
            "\10\1\1\uffff\2\1\2\uffff\3\1\1\30\22\uffff\3\1\21\uffff\2\1"+
            "\2\uffff\6\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA91_eot = DFA.unpackEncodedString(DFA91_eotS);
    static final short[] DFA91_eof = DFA.unpackEncodedString(DFA91_eofS);
    static final char[] DFA91_min = DFA.unpackEncodedStringToUnsignedChars(DFA91_minS);
    static final char[] DFA91_max = DFA.unpackEncodedStringToUnsignedChars(DFA91_maxS);
    static final short[] DFA91_accept = DFA.unpackEncodedString(DFA91_acceptS);
    static final short[] DFA91_special = DFA.unpackEncodedString(DFA91_specialS);
    static final short[][] DFA91_transition;

    static {
        int numStates = DFA91_transitionS.length;
        DFA91_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA91_transition[i] = DFA.unpackEncodedString(DFA91_transitionS[i]);
        }
    }

    class DFA91 extends DFA {

        public DFA91(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 91;
            this.eot = DFA91_eot;
            this.eof = DFA91_eof;
            this.min = DFA91_min;
            this.max = DFA91_max;
            this.accept = DFA91_accept;
            this.special = DFA91_special;
            this.transition = DFA91_transition;
        }
        public String getDescription() {
            return "568:1: elementValue : ( conditionalExpression | annotation | elementValueArrayInitializer );";
        }
    }
    static final String DFA93_eotS =
        "\33\uffff";
    static final String DFA93_eofS =
        "\33\uffff";
    static final String DFA93_minS =
        "\1\4\32\uffff";
    static final String DFA93_maxS =
        "\1\167\32\uffff";
    static final String DFA93_acceptS =
        "\1\uffff\1\1\30\uffff\1\2";
    static final String DFA93_specialS =
        "\33\uffff}>";
    static final String[] DFA93_transitionS = {
            "\1\1\1\uffff\6\1\25\uffff\1\1\3\uffff\1\1\1\32\1\uffff\1\1"+
            "\16\uffff\10\1\1\uffff\2\1\2\uffff\4\1\22\uffff\3\1\21\uffff"+
            "\2\1\2\uffff\6\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA93_eot = DFA.unpackEncodedString(DFA93_eotS);
    static final short[] DFA93_eof = DFA.unpackEncodedString(DFA93_eofS);
    static final char[] DFA93_min = DFA.unpackEncodedStringToUnsignedChars(DFA93_minS);
    static final char[] DFA93_max = DFA.unpackEncodedStringToUnsignedChars(DFA93_maxS);
    static final short[] DFA93_accept = DFA.unpackEncodedString(DFA93_acceptS);
    static final short[] DFA93_special = DFA.unpackEncodedString(DFA93_specialS);
    static final short[][] DFA93_transition;

    static {
        int numStates = DFA93_transitionS.length;
        DFA93_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA93_transition[i] = DFA.unpackEncodedString(DFA93_transitionS[i]);
        }
    }

    class DFA93 extends DFA {

        public DFA93(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 93;
            this.eot = DFA93_eot;
            this.eof = DFA93_eof;
            this.min = DFA93_min;
            this.max = DFA93_max;
            this.accept = DFA93_accept;
            this.special = DFA93_special;
            this.transition = DFA93_transition;
        }
        public String getDescription() {
            return "575:8: ( elementValue ( ',' elementValue )* )?";
        }
    }
    static final String DFA94_eotS =
        "\23\uffff";
    static final String DFA94_eofS =
        "\23\uffff";
    static final String DFA94_minS =
        "\1\4\22\uffff";
    static final String DFA94_maxS =
        "\1\107\22\uffff";
    static final String DFA94_acceptS =
        "\1\uffff\1\1\20\uffff\1\2";
    static final String DFA94_specialS =
        "\23\uffff}>";
    static final String[] DFA94_transitionS = {
            "\2\1\25\uffff\1\1\2\uffff\1\1\7\uffff\1\22\1\1\5\uffff\22\1"+
            "\10\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA94_eot = DFA.unpackEncodedString(DFA94_eotS);
    static final short[] DFA94_eof = DFA.unpackEncodedString(DFA94_eofS);
    static final char[] DFA94_min = DFA.unpackEncodedStringToUnsignedChars(DFA94_minS);
    static final char[] DFA94_max = DFA.unpackEncodedStringToUnsignedChars(DFA94_maxS);
    static final short[] DFA94_accept = DFA.unpackEncodedString(DFA94_acceptS);
    static final short[] DFA94_special = DFA.unpackEncodedString(DFA94_specialS);
    static final short[][] DFA94_transition;

    static {
        int numStates = DFA94_transitionS.length;
        DFA94_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA94_transition[i] = DFA.unpackEncodedString(DFA94_transitionS[i]);
        }
    }

    class DFA94 extends DFA {

        public DFA94(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 94;
            this.eot = DFA94_eot;
            this.eof = DFA94_eof;
            this.min = DFA94_min;
            this.max = DFA94_max;
            this.accept = DFA94_accept;
            this.special = DFA94_special;
            this.transition = DFA94_transition;
        }
        public String getDescription() {
            return "583:8: ( annotationTypeElementDeclarations )?";
        }
    }
    static final String DFA95_eotS =
        "\24\uffff";
    static final String DFA95_eofS =
        "\1\1\23\uffff";
    static final String DFA95_minS =
        "\1\4\23\uffff";
    static final String DFA95_maxS =
        "\1\107\23\uffff";
    static final String DFA95_acceptS =
        "\1\uffff\1\2\1\uffff\1\1\20\uffff";
    static final String DFA95_specialS =
        "\24\uffff}>";
    static final String[] DFA95_transitionS = {
            "\2\3\25\uffff\1\3\2\uffff\1\3\7\uffff\1\1\1\3\5\uffff\22\3"+
            "\10\uffff\1\3",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA95_eot = DFA.unpackEncodedString(DFA95_eotS);
    static final short[] DFA95_eof = DFA.unpackEncodedString(DFA95_eofS);
    static final char[] DFA95_min = DFA.unpackEncodedStringToUnsignedChars(DFA95_minS);
    static final char[] DFA95_max = DFA.unpackEncodedStringToUnsignedChars(DFA95_maxS);
    static final short[] DFA95_accept = DFA.unpackEncodedString(DFA95_acceptS);
    static final short[] DFA95_special = DFA.unpackEncodedString(DFA95_specialS);
    static final short[][] DFA95_transition;

    static {
        int numStates = DFA95_transitionS.length;
        DFA95_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA95_transition[i] = DFA.unpackEncodedString(DFA95_transitionS[i]);
        }
    }

    class DFA95 extends DFA {

        public DFA95(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 95;
            this.eot = DFA95_eot;
            this.eof = DFA95_eof;
            this.min = DFA95_min;
            this.max = DFA95_max;
            this.accept = DFA95_accept;
            this.special = DFA95_special;
            this.transition = DFA95_transition;
        }
        public String getDescription() {
            return "()* loopback of 587:39: ( annotationTypeElementDeclaration )*";
        }
    }
    static final String DFA96_eotS =
        "\24\uffff";
    static final String DFA96_eofS =
        "\24\uffff";
    static final String DFA96_minS =
        "\1\4\5\uffff\1\4\15\uffff";
    static final String DFA96_maxS =
        "\1\107\5\uffff\1\47\15\uffff";
    static final String DFA96_acceptS =
        "\1\uffff\1\2\5\uffff\1\1\14\uffff";
    static final String DFA96_specialS =
        "\24\uffff}>";
    static final String[] DFA96_transitionS = {
            "\2\1\25\uffff\1\7\2\uffff\1\1\10\uffff\1\1\5\uffff\12\7\10"+
            "\1\10\uffff\1\6",
            "",
            "",
            "",
            "",
            "",
            "\1\7\42\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA96_eot = DFA.unpackEncodedString(DFA96_eotS);
    static final short[] DFA96_eof = DFA.unpackEncodedString(DFA96_eofS);
    static final char[] DFA96_min = DFA.unpackEncodedStringToUnsignedChars(DFA96_minS);
    static final char[] DFA96_max = DFA.unpackEncodedStringToUnsignedChars(DFA96_maxS);
    static final short[] DFA96_accept = DFA.unpackEncodedString(DFA96_acceptS);
    static final short[] DFA96_special = DFA.unpackEncodedString(DFA96_specialS);
    static final short[][] DFA96_transition;

    static {
        int numStates = DFA96_transitionS.length;
        DFA96_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA96_transition[i] = DFA.unpackEncodedString(DFA96_transitionS[i]);
        }
    }

    class DFA96 extends DFA {

        public DFA96(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 96;
            this.eot = DFA96_eot;
            this.eof = DFA96_eof;
            this.min = DFA96_min;
            this.max = DFA96_max;
            this.accept = DFA96_accept;
            this.special = DFA96_special;
            this.transition = DFA96_transition;
        }
        public String getDescription() {
            return "()* loopback of 591:4: ( modifier )*";
        }
    }
    static final String DFA101_eotS =
        "\13\uffff";
    static final String DFA101_eofS =
        "\13\uffff";
    static final String DFA101_minS =
        "\1\4\3\uffff\1\4\1\uffff\1\47\2\0\2\uffff";
    static final String DFA101_maxS =
        "\1\107\3\uffff\1\4\1\uffff\1\47\2\0\2\uffff";
    static final String DFA101_acceptS =
        "\1\uffff\1\1\1\uffff\1\2\1\uffff\1\3\3\uffff\1\4\1\5";
    static final String DFA101_specialS =
        "\7\uffff\1\0\1\1\2\uffff}>";
    static final String[] DFA101_transitionS = {
            "\1\1\1\4\30\uffff\1\3\10\uffff\1\5\17\uffff\10\1\10\uffff\1"+
            "\6",
            "",
            "",
            "",
            "\1\7",
            "",
            "\1\10",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA101_eot = DFA.unpackEncodedString(DFA101_eotS);
    static final short[] DFA101_eof = DFA.unpackEncodedString(DFA101_eofS);
    static final char[] DFA101_min = DFA.unpackEncodedStringToUnsignedChars(DFA101_minS);
    static final char[] DFA101_max = DFA.unpackEncodedStringToUnsignedChars(DFA101_maxS);
    static final short[] DFA101_accept = DFA.unpackEncodedString(DFA101_acceptS);
    static final short[] DFA101_special = DFA.unpackEncodedString(DFA101_specialS);
    static final short[][] DFA101_transition;

    static {
        int numStates = DFA101_transitionS.length;
        DFA101_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA101_transition[i] = DFA.unpackEncodedString(DFA101_transitionS[i]);
        }
    }

    class DFA101 extends DFA {

        public DFA101(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 101;
            this.eot = DFA101_eot;
            this.eof = DFA101_eof;
            this.min = DFA101_min;
            this.max = DFA101_max;
            this.accept = DFA101_accept;
            this.special = DFA101_special;
            this.transition = DFA101_transition;
        }
        public String getDescription() {
            return "594:1: annotationTypeElementRest : ( type annotationMethodOrConstantRest ';' | classDeclaration ( ';' )? | interfaceDeclaration ( ';' )? | enumDeclaration ( ';' )? | annotationTypeDeclaration ( ';' )? );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA101_7 = input.LA(1);

                         
                        int index101_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred135_Java()) ) {s = 3;}

                        else if ( (synpred139_Java()) ) {s = 9;}

                         
                        input.seek(index101_7);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA101_8 = input.LA(1);

                         
                        int index101_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred137_Java()) ) {s = 5;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index101_8);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 101, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA97_eotS =
        "\25\uffff";
    static final String DFA97_eofS =
        "\1\2\24\uffff";
    static final String DFA97_minS =
        "\1\4\24\uffff";
    static final String DFA97_maxS =
        "\1\107\24\uffff";
    static final String DFA97_acceptS =
        "\1\uffff\1\1\1\2\22\uffff";
    static final String DFA97_specialS =
        "\25\uffff}>";
    static final String[] DFA97_transitionS = {
            "\2\2\23\uffff\1\1\1\uffff\1\2\2\uffff\1\2\7\uffff\2\2\5\uffff"+
            "\22\2\10\uffff\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA97_eot = DFA.unpackEncodedString(DFA97_eotS);
    static final short[] DFA97_eof = DFA.unpackEncodedString(DFA97_eofS);
    static final char[] DFA97_min = DFA.unpackEncodedStringToUnsignedChars(DFA97_minS);
    static final char[] DFA97_max = DFA.unpackEncodedStringToUnsignedChars(DFA97_maxS);
    static final short[] DFA97_accept = DFA.unpackEncodedString(DFA97_acceptS);
    static final short[] DFA97_special = DFA.unpackEncodedString(DFA97_specialS);
    static final short[][] DFA97_transition;

    static {
        int numStates = DFA97_transitionS.length;
        DFA97_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA97_transition[i] = DFA.unpackEncodedString(DFA97_transitionS[i]);
        }
    }

    class DFA97 extends DFA {

        public DFA97(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 97;
            this.eot = DFA97_eot;
            this.eof = DFA97_eof;
            this.min = DFA97_min;
            this.max = DFA97_max;
            this.accept = DFA97_accept;
            this.special = DFA97_special;
            this.transition = DFA97_transition;
        }
        public String getDescription() {
            return "596:23: ( ';' )?";
        }
    }
    static final String DFA98_eotS =
        "\25\uffff";
    static final String DFA98_eofS =
        "\1\2\24\uffff";
    static final String DFA98_minS =
        "\1\4\24\uffff";
    static final String DFA98_maxS =
        "\1\107\24\uffff";
    static final String DFA98_acceptS =
        "\1\uffff\1\1\1\2\22\uffff";
    static final String DFA98_specialS =
        "\25\uffff}>";
    static final String[] DFA98_transitionS = {
            "\2\2\23\uffff\1\1\1\uffff\1\2\2\uffff\1\2\7\uffff\2\2\5\uffff"+
            "\22\2\10\uffff\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA98_eot = DFA.unpackEncodedString(DFA98_eotS);
    static final short[] DFA98_eof = DFA.unpackEncodedString(DFA98_eofS);
    static final char[] DFA98_min = DFA.unpackEncodedStringToUnsignedChars(DFA98_minS);
    static final char[] DFA98_max = DFA.unpackEncodedStringToUnsignedChars(DFA98_maxS);
    static final short[] DFA98_accept = DFA.unpackEncodedString(DFA98_acceptS);
    static final short[] DFA98_special = DFA.unpackEncodedString(DFA98_specialS);
    static final short[][] DFA98_transition;

    static {
        int numStates = DFA98_transitionS.length;
        DFA98_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA98_transition[i] = DFA.unpackEncodedString(DFA98_transitionS[i]);
        }
    }

    class DFA98 extends DFA {

        public DFA98(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 98;
            this.eot = DFA98_eot;
            this.eof = DFA98_eof;
            this.min = DFA98_min;
            this.max = DFA98_max;
            this.accept = DFA98_accept;
            this.special = DFA98_special;
            this.transition = DFA98_transition;
        }
        public String getDescription() {
            return "597:27: ( ';' )?";
        }
    }
    static final String DFA99_eotS =
        "\25\uffff";
    static final String DFA99_eofS =
        "\1\2\24\uffff";
    static final String DFA99_minS =
        "\1\4\24\uffff";
    static final String DFA99_maxS =
        "\1\107\24\uffff";
    static final String DFA99_acceptS =
        "\1\uffff\1\1\1\2\22\uffff";
    static final String DFA99_specialS =
        "\25\uffff}>";
    static final String[] DFA99_transitionS = {
            "\2\2\23\uffff\1\1\1\uffff\1\2\2\uffff\1\2\7\uffff\2\2\5\uffff"+
            "\22\2\10\uffff\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA99_eot = DFA.unpackEncodedString(DFA99_eotS);
    static final short[] DFA99_eof = DFA.unpackEncodedString(DFA99_eofS);
    static final char[] DFA99_min = DFA.unpackEncodedStringToUnsignedChars(DFA99_minS);
    static final char[] DFA99_max = DFA.unpackEncodedStringToUnsignedChars(DFA99_maxS);
    static final short[] DFA99_accept = DFA.unpackEncodedString(DFA99_acceptS);
    static final short[] DFA99_special = DFA.unpackEncodedString(DFA99_specialS);
    static final short[][] DFA99_transition;

    static {
        int numStates = DFA99_transitionS.length;
        DFA99_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA99_transition[i] = DFA.unpackEncodedString(DFA99_transitionS[i]);
        }
    }

    class DFA99 extends DFA {

        public DFA99(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 99;
            this.eot = DFA99_eot;
            this.eof = DFA99_eof;
            this.min = DFA99_min;
            this.max = DFA99_max;
            this.accept = DFA99_accept;
            this.special = DFA99_special;
            this.transition = DFA99_transition;
        }
        public String getDescription() {
            return "598:22: ( ';' )?";
        }
    }
    static final String DFA100_eotS =
        "\25\uffff";
    static final String DFA100_eofS =
        "\1\2\24\uffff";
    static final String DFA100_minS =
        "\1\4\24\uffff";
    static final String DFA100_maxS =
        "\1\107\24\uffff";
    static final String DFA100_acceptS =
        "\1\uffff\1\1\1\2\22\uffff";
    static final String DFA100_specialS =
        "\25\uffff}>";
    static final String[] DFA100_transitionS = {
            "\2\2\23\uffff\1\1\1\uffff\1\2\2\uffff\1\2\7\uffff\2\2\5\uffff"+
            "\22\2\10\uffff\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA100_eot = DFA.unpackEncodedString(DFA100_eotS);
    static final short[] DFA100_eof = DFA.unpackEncodedString(DFA100_eofS);
    static final char[] DFA100_min = DFA.unpackEncodedStringToUnsignedChars(DFA100_minS);
    static final char[] DFA100_max = DFA.unpackEncodedStringToUnsignedChars(DFA100_maxS);
    static final short[] DFA100_accept = DFA.unpackEncodedString(DFA100_acceptS);
    static final short[] DFA100_special = DFA.unpackEncodedString(DFA100_specialS);
    static final short[][] DFA100_transition;

    static {
        int numStates = DFA100_transitionS.length;
        DFA100_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA100_transition[i] = DFA.unpackEncodedString(DFA100_transitionS[i]);
        }
    }

    class DFA100 extends DFA {

        public DFA100(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 100;
            this.eot = DFA100_eot;
            this.eof = DFA100_eof;
            this.min = DFA100_min;
            this.max = DFA100_max;
            this.accept = DFA100_accept;
            this.special = DFA100_special;
            this.transition = DFA100_transition;
        }
        public String getDescription() {
            return "599:32: ( ';' )?";
        }
    }
    static final String DFA104_eotS =
        "\66\uffff";
    static final String DFA104_eofS =
        "\66\uffff";
    static final String DFA104_minS =
        "\1\4\65\uffff";
    static final String DFA104_maxS =
        "\1\167\65\uffff";
    static final String DFA104_acceptS =
        "\1\uffff\1\2\1\1\63\uffff";
    static final String DFA104_specialS =
        "\66\uffff}>";
    static final String[] DFA104_transitionS = {
            "\10\2\15\uffff\1\2\1\uffff\1\2\2\uffff\1\2\2\uffff\1\2\3\uffff"+
            "\1\2\1\1\2\2\4\uffff\22\2\1\uffff\2\2\2\uffff\4\2\1\uffff\1"+
            "\2\1\uffff\1\2\1\uffff\4\2\1\uffff\6\2\2\uffff\3\2\21\uffff"+
            "\2\2\2\uffff\6\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA104_eot = DFA.unpackEncodedString(DFA104_eotS);
    static final short[] DFA104_eof = DFA.unpackEncodedString(DFA104_eofS);
    static final char[] DFA104_min = DFA.unpackEncodedStringToUnsignedChars(DFA104_minS);
    static final char[] DFA104_max = DFA.unpackEncodedStringToUnsignedChars(DFA104_maxS);
    static final short[] DFA104_accept = DFA.unpackEncodedString(DFA104_acceptS);
    static final short[] DFA104_special = DFA.unpackEncodedString(DFA104_specialS);
    static final short[][] DFA104_transition;

    static {
        int numStates = DFA104_transitionS.length;
        DFA104_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA104_transition[i] = DFA.unpackEncodedString(DFA104_transitionS[i]);
        }
    }

    class DFA104 extends DFA {

        public DFA104(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 104;
            this.eot = DFA104_eot;
            this.eof = DFA104_eof;
            this.min = DFA104_min;
            this.max = DFA104_max;
            this.accept = DFA104_accept;
            this.special = DFA104_special;
            this.transition = DFA104_transition;
        }
        public String getDescription() {
            return "()* loopback of 628:8: ( blockStatement )*";
        }
    }
    static final String DFA105_eotS =
        "\167\uffff";
    static final String DFA105_eofS =
        "\167\uffff";
    static final String DFA105_minS =
        "\5\4\6\uffff\1\5\53\uffff\2\0\16\uffff\1\0\1\uffff\3\0\30\uffff"+
        "\1\0\22\uffff";
    static final String DFA105_maxS =
        "\1\167\1\107\1\47\1\163\1\51\6\uffff\1\107\53\uffff\2\0\16\uffff"+
        "\1\0\1\uffff\3\0\30\uffff\1\0\22\uffff";
    static final String DFA105_acceptS =
        "\5\uffff\1\2\14\uffff\1\3\42\uffff\1\1\101\uffff";
    static final String DFA105_specialS =
        "\67\uffff\1\0\1\1\16\uffff\1\2\1\uffff\1\3\1\4\1\5\30\uffff\1\6"+
        "\22\uffff}>";
    static final String[] DFA105_transitionS = {
            "\1\3\1\5\6\22\15\uffff\1\22\1\uffff\1\5\2\uffff\1\5\2\uffff"+
            "\1\22\3\uffff\1\22\1\uffff\1\5\1\22\4\uffff\4\5\1\1\1\5\1\13"+
            "\3\5\10\4\1\uffff\2\22\2\uffff\3\22\1\2\1\uffff\1\22\1\uffff"+
            "\1\22\1\uffff\4\22\1\uffff\6\22\2\uffff\3\22\21\uffff\2\22\2"+
            "\uffff\6\22",
            "\1\65\1\5\25\uffff\1\5\2\uffff\1\5\10\uffff\1\5\5\uffff\4"+
            "\5\1\67\5\5\10\65\10\uffff\1\70",
            "\1\107\42\uffff\1\5",
            "\1\65\24\uffff\1\22\2\uffff\1\112\1\22\3\uffff\1\111\1\uffff"+
            "\2\22\4\uffff\1\113\2\uffff\1\22\22\uffff\1\22\1\uffff\1\22"+
            "\10\uffff\1\22\24\uffff\25\22",
            "\1\65\27\uffff\1\22\14\uffff\1\144",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\5\25\uffff\1\5\2\uffff\1\5\10\uffff\1\5\5\uffff\12\5\12"+
            "\uffff\1\22\5\uffff\1\5",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA105_eot = DFA.unpackEncodedString(DFA105_eotS);
    static final short[] DFA105_eof = DFA.unpackEncodedString(DFA105_eofS);
    static final char[] DFA105_min = DFA.unpackEncodedStringToUnsignedChars(DFA105_minS);
    static final char[] DFA105_max = DFA.unpackEncodedStringToUnsignedChars(DFA105_maxS);
    static final short[] DFA105_accept = DFA.unpackEncodedString(DFA105_acceptS);
    static final short[] DFA105_special = DFA.unpackEncodedString(DFA105_specialS);
    static final short[][] DFA105_transition;

    static {
        int numStates = DFA105_transitionS.length;
        DFA105_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA105_transition[i] = DFA.unpackEncodedString(DFA105_transitionS[i]);
        }
    }

    class DFA105 extends DFA {

        public DFA105(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 105;
            this.eot = DFA105_eot;
            this.eof = DFA105_eof;
            this.min = DFA105_min;
            this.max = DFA105_max;
            this.accept = DFA105_accept;
            this.special = DFA105_special;
            this.transition = DFA105_transition;
        }
        public String getDescription() {
            return "631:1: blockStatement : ( localVariableDeclaration | classOrInterfaceDeclaration | statement );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA105_55 = input.LA(1);

                         
                        int index105_55 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred144_Java()) ) {s = 53;}

                        else if ( (synpred145_Java()) ) {s = 5;}

                         
                        input.seek(index105_55);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA105_56 = input.LA(1);

                         
                        int index105_56 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred144_Java()) ) {s = 53;}

                        else if ( (synpred145_Java()) ) {s = 5;}

                         
                        input.seek(index105_56);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA105_71 = input.LA(1);

                         
                        int index105_71 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred144_Java()) ) {s = 53;}

                        else if ( (synpred145_Java()) ) {s = 5;}

                         
                        input.seek(index105_71);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA105_73 = input.LA(1);

                         
                        int index105_73 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred144_Java()) ) {s = 53;}

                        else if ( (true) ) {s = 18;}

                         
                        input.seek(index105_73);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA105_74 = input.LA(1);

                         
                        int index105_74 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred144_Java()) ) {s = 53;}

                        else if ( (true) ) {s = 18;}

                         
                        input.seek(index105_74);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA105_75 = input.LA(1);

                         
                        int index105_75 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred144_Java()) ) {s = 53;}

                        else if ( (true) ) {s = 18;}

                         
                        input.seek(index105_75);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA105_100 = input.LA(1);

                         
                        int index105_100 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred144_Java()) ) {s = 53;}

                        else if ( (true) ) {s = 18;}

                         
                        input.seek(index105_100);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 105, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA113_eotS =
        "\102\uffff";
    static final String DFA113_eofS =
        "\102\uffff";
    static final String DFA113_minS =
        "\1\4\43\uffff\1\31\35\uffff";
    static final String DFA113_maxS =
        "\1\167\43\uffff\1\163\35\uffff";
    static final String DFA113_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
        "\15\1\16\1\17\1\20\26\uffff\1\21\32\uffff";
    static final String DFA113_specialS =
        "\102\uffff}>";
    static final String[] DFA113_transitionS = {
            "\1\44\1\uffff\6\20\15\uffff\1\17\7\uffff\1\20\3\uffff\1\1\2"+
            "\uffff\1\20\12\uffff\1\11\3\uffff\10\20\1\uffff\2\20\2\uffff"+
            "\3\20\2\uffff\1\2\1\uffff\1\3\1\uffff\1\4\1\5\1\6\1\7\1\uffff"+
            "\1\10\1\12\1\13\1\14\1\15\1\16\2\uffff\3\20\21\uffff\2\20\2"+
            "\uffff\6\20",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\20\2\uffff\2\20\3\uffff\1\20\1\uffff\2\20\4\uffff\1\20"+
            "\2\uffff\1\20\22\uffff\1\20\1\uffff\1\20\10\uffff\1\47\24\uffff"+
            "\25\20",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA113_eot = DFA.unpackEncodedString(DFA113_eotS);
    static final short[] DFA113_eof = DFA.unpackEncodedString(DFA113_eofS);
    static final char[] DFA113_min = DFA.unpackEncodedStringToUnsignedChars(DFA113_minS);
    static final char[] DFA113_max = DFA.unpackEncodedStringToUnsignedChars(DFA113_maxS);
    static final short[] DFA113_accept = DFA.unpackEncodedString(DFA113_acceptS);
    static final short[] DFA113_special = DFA.unpackEncodedString(DFA113_specialS);
    static final short[][] DFA113_transition;

    static {
        int numStates = DFA113_transitionS.length;
        DFA113_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA113_transition[i] = DFA.unpackEncodedString(DFA113_transitionS[i]);
        }
    }

    class DFA113 extends DFA {

        public DFA113(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 113;
            this.eot = DFA113_eot;
            this.eof = DFA113_eof;
            this.min = DFA113_min;
            this.max = DFA113_max;
            this.accept = DFA113_accept;
            this.special = DFA113_special;
            this.transition = DFA113_transition;
        }
        public String getDescription() {
            return "663:1: statement : ( block | 'assert' expression ( ':' expression )? ';' | 'if' parExpression statement ( options {k=1; } : 'else' statement )? | 'for' '(' forControl ')' statement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | 'try' block ( catches 'finally' block | catches | 'finally' block ) | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( Identifier )? ';' | 'continue' ( Identifier )? ';' | modifyStatement | ';' | statementExpression ';' | Identifier ':' statement );";
        }
    }
    static final String DFA110_eotS =
        "\31\uffff";
    static final String DFA110_eofS =
        "\31\uffff";
    static final String DFA110_minS =
        "\1\4\30\uffff";
    static final String DFA110_maxS =
        "\1\167\30\uffff";
    static final String DFA110_acceptS =
        "\1\uffff\1\1\26\uffff\1\2";
    static final String DFA110_specialS =
        "\31\uffff}>";
    static final String[] DFA110_transitionS = {
            "\1\1\1\uffff\6\1\15\uffff\1\30\7\uffff\1\1\6\uffff\1\1\16\uffff"+
            "\10\1\1\uffff\2\1\2\uffff\3\1\23\uffff\3\1\21\uffff\2\1\2\uffff"+
            "\6\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA110_eot = DFA.unpackEncodedString(DFA110_eotS);
    static final short[] DFA110_eof = DFA.unpackEncodedString(DFA110_eofS);
    static final char[] DFA110_min = DFA.unpackEncodedStringToUnsignedChars(DFA110_minS);
    static final char[] DFA110_max = DFA.unpackEncodedStringToUnsignedChars(DFA110_maxS);
    static final short[] DFA110_accept = DFA.unpackEncodedString(DFA110_acceptS);
    static final short[] DFA110_special = DFA.unpackEncodedString(DFA110_specialS);
    static final short[][] DFA110_transition;

    static {
        int numStates = DFA110_transitionS.length;
        DFA110_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA110_transition[i] = DFA.unpackEncodedString(DFA110_transitionS[i]);
        }
    }

    class DFA110 extends DFA {

        public DFA110(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 110;
            this.eot = DFA110_eot;
            this.eof = DFA110_eof;
            this.min = DFA110_min;
            this.max = DFA110_max;
            this.accept = DFA110_accept;
            this.special = DFA110_special;
            this.transition = DFA110_transition;
        }
        public String getDescription() {
            return "677:16: ( expression )?";
        }
    }
    static final String DFA115_eotS =
        "\31\uffff";
    static final String DFA115_eofS =
        "\31\uffff";
    static final String DFA115_minS =
        "\1\4\30\uffff";
    static final String DFA115_maxS =
        "\1\167\30\uffff";
    static final String DFA115_acceptS =
        "\1\uffff\1\1\26\uffff\1\2";
    static final String DFA115_specialS =
        "\31\uffff}>";
    static final String[] DFA115_transitionS = {
            "\1\1\1\uffff\6\1\25\uffff\1\1\4\uffff\1\30\1\uffff\1\1\16\uffff"+
            "\10\1\1\uffff\2\1\2\uffff\3\1\23\uffff\3\1\21\uffff\2\1\2\uffff"+
            "\6\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA115_eot = DFA.unpackEncodedString(DFA115_eotS);
    static final short[] DFA115_eof = DFA.unpackEncodedString(DFA115_eofS);
    static final char[] DFA115_min = DFA.unpackEncodedStringToUnsignedChars(DFA115_minS);
    static final char[] DFA115_max = DFA.unpackEncodedStringToUnsignedChars(DFA115_maxS);
    static final short[] DFA115_accept = DFA.unpackEncodedString(DFA115_acceptS);
    static final short[] DFA115_special = DFA.unpackEncodedString(DFA115_specialS);
    static final short[][] DFA115_transition;

    static {
        int numStates = DFA115_transitionS.length;
        DFA115_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA115_transition[i] = DFA.unpackEncodedString(DFA115_transitionS[i]);
        }
    }

    class DFA115 extends DFA {

        public DFA115(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 115;
            this.eot = DFA115_eot;
            this.eof = DFA115_eof;
            this.min = DFA115_min;
            this.max = DFA115_max;
            this.accept = DFA115_accept;
            this.special = DFA115_special;
            this.transition = DFA115_transition;
        }
        public String getDescription() {
            return "699:6: (e= expression ( ',' e= expression )* )?";
        }
    }
    static final String DFA117_eotS =
        "\74\uffff";
    static final String DFA117_eofS =
        "\1\1\73\uffff";
    static final String DFA117_minS =
        "\1\4\73\uffff";
    static final String DFA117_maxS =
        "\1\167\73\uffff";
    static final String DFA117_acceptS =
        "\1\uffff\1\2\71\uffff\1\1";
    static final String DFA117_specialS =
        "\74\uffff}>";
    static final String[] DFA117_transitionS = {
            "\10\1\15\uffff\1\1\1\uffff\1\1\2\uffff\1\1\2\uffff\1\1\3\uffff"+
            "\4\1\4\uffff\22\1\1\uffff\2\1\2\uffff\6\1\1\uffff\15\1\2\uffff"+
            "\3\1\1\73\1\1\17\uffff\2\1\2\uffff\6\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA117_eot = DFA.unpackEncodedString(DFA117_eotS);
    static final short[] DFA117_eof = DFA.unpackEncodedString(DFA117_eofS);
    static final char[] DFA117_min = DFA.unpackEncodedStringToUnsignedChars(DFA117_minS);
    static final char[] DFA117_max = DFA.unpackEncodedStringToUnsignedChars(DFA117_maxS);
    static final short[] DFA117_accept = DFA.unpackEncodedString(DFA117_acceptS);
    static final short[] DFA117_special = DFA.unpackEncodedString(DFA117_specialS);
    static final short[][] DFA117_transition;

    static {
        int numStates = DFA117_transitionS.length;
        DFA117_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA117_transition[i] = DFA.unpackEncodedString(DFA117_transitionS[i]);
        }
    }

    class DFA117 extends DFA {

        public DFA117(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 117;
            this.eot = DFA117_eot;
            this.eof = DFA117_eof;
            this.min = DFA117_min;
            this.max = DFA117_max;
            this.accept = DFA117_accept;
            this.special = DFA117_special;
            this.transition = DFA117_transition;
        }
        public String getDescription() {
            return "()* loopback of 772:16: ( catchClause )*";
        }
    }
    static final String DFA120_eotS =
        "\71\uffff";
    static final String DFA120_eofS =
        "\1\1\70\uffff";
    static final String DFA120_minS =
        "\1\4\70\uffff";
    static final String DFA120_maxS =
        "\1\167\70\uffff";
    static final String DFA120_acceptS =
        "\1\uffff\1\2\3\uffff\1\1\63\uffff";
    static final String DFA120_specialS =
        "\71\uffff}>";
    static final String[] DFA120_transitionS = {
            "\10\5\15\uffff\1\5\1\uffff\1\5\2\uffff\1\5\2\uffff\1\5\3\uffff"+
            "\1\5\1\1\2\5\4\uffff\22\5\1\uffff\2\5\2\uffff\4\5\1\1\1\5\1"+
            "\uffff\1\5\1\uffff\4\5\1\uffff\6\5\2\uffff\3\5\1\uffff\1\1\17"+
            "\uffff\2\5\2\uffff\6\5",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA120_eot = DFA.unpackEncodedString(DFA120_eotS);
    static final short[] DFA120_eof = DFA.unpackEncodedString(DFA120_eofS);
    static final char[] DFA120_min = DFA.unpackEncodedStringToUnsignedChars(DFA120_minS);
    static final char[] DFA120_max = DFA.unpackEncodedStringToUnsignedChars(DFA120_maxS);
    static final short[] DFA120_accept = DFA.unpackEncodedString(DFA120_acceptS);
    static final short[] DFA120_special = DFA.unpackEncodedString(DFA120_specialS);
    static final short[][] DFA120_transition;

    static {
        int numStates = DFA120_transitionS.length;
        DFA120_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA120_transition[i] = DFA.unpackEncodedString(DFA120_transitionS[i]);
        }
    }

    class DFA120 extends DFA {

        public DFA120(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 120;
            this.eot = DFA120_eot;
            this.eof = DFA120_eof;
            this.min = DFA120_min;
            this.max = DFA120_max;
            this.accept = DFA120_accept;
            this.special = DFA120_special;
            this.transition = DFA120_transition;
        }
        public String getDescription() {
            return "()* loopback of 788:16: ( blockStatement )*";
        }
    }
    static final String DFA121_eotS =
        "\33\uffff";
    static final String DFA121_eofS =
        "\33\uffff";
    static final String DFA121_minS =
        "\1\110\1\4\25\uffff\1\0\3\uffff";
    static final String DFA121_maxS =
        "\1\136\1\167\25\uffff\1\0\3\uffff";
    static final String DFA121_acceptS =
        "\2\uffff\1\3\1\1\26\uffff\1\2";
    static final String DFA121_specialS =
        "\27\uffff\1\0\3\uffff}>";
    static final String[] DFA121_transitionS = {
            "\1\2\25\uffff\1\1",
            "\1\27\1\uffff\6\3\25\uffff\1\3\6\uffff\1\3\16\uffff\10\3\1"+
            "\uffff\2\3\2\uffff\3\3\23\uffff\3\3\21\uffff\2\3\2\uffff\6\3",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA121_eot = DFA.unpackEncodedString(DFA121_eotS);
    static final short[] DFA121_eof = DFA.unpackEncodedString(DFA121_eofS);
    static final char[] DFA121_min = DFA.unpackEncodedStringToUnsignedChars(DFA121_minS);
    static final char[] DFA121_max = DFA.unpackEncodedStringToUnsignedChars(DFA121_maxS);
    static final short[] DFA121_accept = DFA.unpackEncodedString(DFA121_acceptS);
    static final short[] DFA121_special = DFA.unpackEncodedString(DFA121_specialS);
    static final short[][] DFA121_transition;

    static {
        int numStates = DFA121_transitionS.length;
        DFA121_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA121_transition[i] = DFA.unpackEncodedString(DFA121_transitionS[i]);
        }
    }

    class DFA121 extends DFA {

        public DFA121(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 121;
            this.eot = DFA121_eot;
            this.eof = DFA121_eof;
            this.min = DFA121_min;
            this.max = DFA121_max;
            this.accept = DFA121_accept;
            this.special = DFA121_special;
            this.transition = DFA121_transition;
        }
        public String getDescription() {
            return "791:1: switchLabel : ( 'case' constantExpression ':' | 'case' enumConstantName ':' | 'default' ':' );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA121_23 = input.LA(1);

                         
                        int index121_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred178_Java()) ) {s = 3;}

                        else if ( (synpred179_Java()) ) {s = 26;}

                         
                        input.seek(index121_23);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 121, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA126_eotS =
        "\u0092\uffff";
    static final String DFA126_eofS =
        "\u0092\uffff";
    static final String DFA126_minS =
        "\5\4\26\uffff\10\4\1\31\30\uffff\1\52\1\31\1\uffff\21\0\2\uffff"+
        "\3\0\24\uffff\1\0\5\uffff\1\0\34\uffff\1\0\5\uffff";
    static final String DFA126_maxS =
        "\1\167\1\107\1\4\1\163\1\51\26\uffff\2\51\1\107\1\4\1\107\3\167"+
        "\1\112\30\uffff\1\52\1\112\1\uffff\21\0\2\uffff\3\0\24\uffff\1\0"+
        "\5\uffff\1\0\34\uffff\1\0\5\uffff";
    static final String DFA126_acceptS =
        "\5\uffff\1\2\u0081\uffff\1\1\12\uffff";
    static final String DFA126_specialS =
        "\77\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\2\uffff\1\21\1\22\1\23\24\uffff\1\24\5\uffff"+
        "\1\25\34\uffff\1\26\5\uffff}>";
    static final String[] DFA126_transitionS = {
            "\1\3\1\uffff\6\5\15\uffff\1\5\7\uffff\1\5\6\uffff\1\5\10\uffff"+
            "\1\1\5\uffff\10\4\1\uffff\2\5\2\uffff\3\5\1\2\22\uffff\3\5\21"+
            "\uffff\2\5\2\uffff\6\5",
            "\1\33\54\uffff\1\35\5\uffff\10\34\10\uffff\1\36",
            "\1\37",
            "\1\43\24\uffff\1\5\2\uffff\1\41\1\5\3\uffff\1\40\3\5\4\uffff"+
            "\1\42\2\uffff\1\5\22\uffff\1\5\1\uffff\1\5\35\uffff\25\5",
            "\1\75\27\uffff\1\5\14\uffff\1\74",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\102\27\uffff\1\100\4\uffff\1\77\7\uffff\1\101",
            "\1\104\44\uffff\1\103",
            "\1\105\54\uffff\1\107\5\uffff\10\106\10\uffff\1\110",
            "\1\111",
            "\1\114\27\uffff\1\112\24\uffff\1\116\5\uffff\10\115\2\uffff"+
            "\1\113\5\uffff\1\117",
            "\1\122\1\uffff\6\5\25\uffff\1\5\6\uffff\1\5\3\uffff\1\5\12"+
            "\uffff\10\123\1\124\2\5\2\uffff\3\5\23\uffff\3\5\21\uffff\2"+
            "\5\2\uffff\6\5",
            "\1\151\31\uffff\1\5\2\uffff\1\5\36\uffff\1\5\65\uffff\2\5",
            "\1\5\1\uffff\6\5\25\uffff\1\5\6\uffff\1\5\1\uffff\1\157\14"+
            "\uffff\10\5\1\uffff\2\5\2\uffff\3\5\23\uffff\3\5\21\uffff\2"+
            "\5\2\uffff\6\5",
            "\1\5\10\uffff\1\5\6\uffff\1\5\2\uffff\1\5\35\uffff\1\u0087",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u008c",
            "\1\5\10\uffff\1\5\6\uffff\1\5\2\uffff\1\5\35\uffff\1\u0087",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA126_eot = DFA.unpackEncodedString(DFA126_eotS);
    static final short[] DFA126_eof = DFA.unpackEncodedString(DFA126_eofS);
    static final char[] DFA126_min = DFA.unpackEncodedStringToUnsignedChars(DFA126_minS);
    static final char[] DFA126_max = DFA.unpackEncodedStringToUnsignedChars(DFA126_maxS);
    static final short[] DFA126_accept = DFA.unpackEncodedString(DFA126_acceptS);
    static final short[] DFA126_special = DFA.unpackEncodedString(DFA126_specialS);
    static final short[][] DFA126_transition;

    static {
        int numStates = DFA126_transitionS.length;
        DFA126_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA126_transition[i] = DFA.unpackEncodedString(DFA126_transitionS[i]);
        }
    }

    class DFA126 extends DFA {

        public DFA126(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 126;
            this.eot = DFA126_eot;
            this.eof = DFA126_eof;
            this.min = DFA126_min;
            this.max = DFA126_max;
            this.accept = DFA126_accept;
            this.special = DFA126_special;
            this.transition = DFA126_transition;
        }
        public String getDescription() {
            return "801:1: forControl options {k=3; } : ( forVarControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA126_63 = input.LA(1);

                         
                        int index126_63 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_63);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA126_64 = input.LA(1);

                         
                        int index126_64 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_64);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA126_65 = input.LA(1);

                         
                        int index126_65 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_65);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA126_66 = input.LA(1);

                         
                        int index126_66 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_66);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA126_67 = input.LA(1);

                         
                        int index126_67 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_67);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA126_68 = input.LA(1);

                         
                        int index126_68 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_68);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA126_69 = input.LA(1);

                         
                        int index126_69 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_69);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA126_70 = input.LA(1);

                         
                        int index126_70 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_70);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA126_71 = input.LA(1);

                         
                        int index126_71 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_71);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA126_72 = input.LA(1);

                         
                        int index126_72 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_72);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA126_73 = input.LA(1);

                         
                        int index126_73 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_73);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA126_74 = input.LA(1);

                         
                        int index126_74 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_74);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA126_75 = input.LA(1);

                         
                        int index126_75 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_75);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA126_76 = input.LA(1);

                         
                        int index126_76 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_76);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA126_77 = input.LA(1);

                         
                        int index126_77 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_77);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA126_78 = input.LA(1);

                         
                        int index126_78 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_78);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA126_79 = input.LA(1);

                         
                        int index126_79 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_79);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA126_82 = input.LA(1);

                         
                        int index126_82 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_82);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA126_83 = input.LA(1);

                         
                        int index126_83 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_83);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA126_84 = input.LA(1);

                         
                        int index126_84 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_84);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA126_105 = input.LA(1);

                         
                        int index126_105 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_105);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA126_111 = input.LA(1);

                         
                        int index126_111 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_111);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA126_140 = input.LA(1);

                         
                        int index126_140 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred181_Java()) ) {s = 135;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index126_140);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 126, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA123_eotS =
        "\33\uffff";
    static final String DFA123_eofS =
        "\33\uffff";
    static final String DFA123_minS =
        "\1\4\32\uffff";
    static final String DFA123_maxS =
        "\1\167\32\uffff";
    static final String DFA123_acceptS =
        "\1\uffff\1\1\30\uffff\1\2";
    static final String DFA123_specialS =
        "\33\uffff}>";
    static final String[] DFA123_transitionS = {
            "\1\1\1\uffff\6\1\15\uffff\1\32\7\uffff\1\1\6\uffff\1\1\10\uffff"+
            "\1\1\5\uffff\10\1\1\uffff\2\1\2\uffff\4\1\22\uffff\3\1\21\uffff"+
            "\2\1\2\uffff\6\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA123_eot = DFA.unpackEncodedString(DFA123_eotS);
    static final short[] DFA123_eof = DFA.unpackEncodedString(DFA123_eofS);
    static final char[] DFA123_min = DFA.unpackEncodedStringToUnsignedChars(DFA123_minS);
    static final char[] DFA123_max = DFA.unpackEncodedStringToUnsignedChars(DFA123_maxS);
    static final short[] DFA123_accept = DFA.unpackEncodedString(DFA123_acceptS);
    static final short[] DFA123_special = DFA.unpackEncodedString(DFA123_specialS);
    static final short[][] DFA123_transition;

    static {
        int numStates = DFA123_transitionS.length;
        DFA123_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA123_transition[i] = DFA.unpackEncodedString(DFA123_transitionS[i]);
        }
    }

    class DFA123 extends DFA {

        public DFA123(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 123;
            this.eot = DFA123_eot;
            this.eof = DFA123_eof;
            this.min = DFA123_min;
            this.max = DFA123_max;
            this.accept = DFA123_accept;
            this.special = DFA123_special;
            this.transition = DFA123_transition;
        }
        public String getDescription() {
            return "804:4: ( forInit )?";
        }
    }
    static final String DFA124_eotS =
        "\31\uffff";
    static final String DFA124_eofS =
        "\31\uffff";
    static final String DFA124_minS =
        "\1\4\30\uffff";
    static final String DFA124_maxS =
        "\1\167\30\uffff";
    static final String DFA124_acceptS =
        "\1\uffff\1\1\26\uffff\1\2";
    static final String DFA124_specialS =
        "\31\uffff}>";
    static final String[] DFA124_transitionS = {
            "\1\1\1\uffff\6\1\15\uffff\1\30\7\uffff\1\1\6\uffff\1\1\16\uffff"+
            "\10\1\1\uffff\2\1\2\uffff\3\1\23\uffff\3\1\21\uffff\2\1\2\uffff"+
            "\6\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA124_eot = DFA.unpackEncodedString(DFA124_eotS);
    static final short[] DFA124_eof = DFA.unpackEncodedString(DFA124_eofS);
    static final char[] DFA124_min = DFA.unpackEncodedStringToUnsignedChars(DFA124_minS);
    static final char[] DFA124_max = DFA.unpackEncodedStringToUnsignedChars(DFA124_maxS);
    static final short[] DFA124_accept = DFA.unpackEncodedString(DFA124_acceptS);
    static final short[] DFA124_special = DFA.unpackEncodedString(DFA124_specialS);
    static final short[][] DFA124_transition;

    static {
        int numStates = DFA124_transitionS.length;
        DFA124_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA124_transition[i] = DFA.unpackEncodedString(DFA124_transitionS[i]);
        }
    }

    class DFA124 extends DFA {

        public DFA124(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 124;
            this.eot = DFA124_eot;
            this.eof = DFA124_eof;
            this.min = DFA124_min;
            this.max = DFA124_max;
            this.accept = DFA124_accept;
            this.special = DFA124_special;
            this.transition = DFA124_transition;
        }
        public String getDescription() {
            return "804:17: ( expression )?";
        }
    }
    static final String DFA125_eotS =
        "\31\uffff";
    static final String DFA125_eofS =
        "\31\uffff";
    static final String DFA125_minS =
        "\1\4\30\uffff";
    static final String DFA125_maxS =
        "\1\167\30\uffff";
    static final String DFA125_acceptS =
        "\1\uffff\1\1\26\uffff\1\2";
    static final String DFA125_specialS =
        "\31\uffff}>";
    static final String[] DFA125_transitionS = {
            "\1\1\1\uffff\6\1\25\uffff\1\1\6\uffff\1\1\16\uffff\10\1\1\uffff"+
            "\2\1\1\30\1\uffff\3\1\23\uffff\3\1\21\uffff\2\1\2\uffff\6\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA125_eot = DFA.unpackEncodedString(DFA125_eotS);
    static final short[] DFA125_eof = DFA.unpackEncodedString(DFA125_eofS);
    static final char[] DFA125_min = DFA.unpackEncodedStringToUnsignedChars(DFA125_minS);
    static final char[] DFA125_max = DFA.unpackEncodedStringToUnsignedChars(DFA125_maxS);
    static final short[] DFA125_accept = DFA.unpackEncodedString(DFA125_acceptS);
    static final short[] DFA125_special = DFA.unpackEncodedString(DFA125_specialS);
    static final short[][] DFA125_transition;

    static {
        int numStates = DFA125_transitionS.length;
        DFA125_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA125_transition[i] = DFA.unpackEncodedString(DFA125_transitionS[i]);
        }
    }

    class DFA125 extends DFA {

        public DFA125(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 125;
            this.eot = DFA125_eot;
            this.eof = DFA125_eof;
            this.min = DFA125_min;
            this.max = DFA125_max;
            this.accept = DFA125_accept;
            this.special = DFA125_special;
            this.transition = DFA125_transition;
        }
        public String getDescription() {
            return "804:33: ( forUpdate )?";
        }
    }
    static final String DFA128_eotS =
        "\72\uffff";
    static final String DFA128_eofS =
        "\3\uffff\1\5\66\uffff";
    static final String DFA128_minS =
        "\1\4\2\uffff\2\4\25\uffff\3\0\32\uffff\1\0\2\uffff";
    static final String DFA128_maxS =
        "\1\167\2\uffff\1\163\1\51\25\uffff\3\0\32\uffff\1\0\2\uffff";
    static final String DFA128_acceptS =
        "\1\uffff\1\1\3\uffff\1\2\64\uffff";
    static final String DFA128_specialS =
        "\32\uffff\1\0\1\1\1\2\32\uffff\1\3\2\uffff}>";
    static final String[] DFA128_transitionS = {
            "\1\3\1\uffff\6\5\25\uffff\1\5\6\uffff\1\5\10\uffff\1\1\5\uffff"+
            "\10\4\1\uffff\2\5\2\uffff\3\5\1\1\22\uffff\3\5\21\uffff\2\5"+
            "\2\uffff\6\5",
            "",
            "",
            "\1\1\24\uffff\1\5\2\uffff\1\33\1\5\3\uffff\1\32\3\5\4\uffff"+
            "\1\34\2\uffff\1\5\22\uffff\1\5\1\uffff\1\5\35\uffff\25\5",
            "\1\1\27\uffff\1\5\14\uffff\1\67",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA128_eot = DFA.unpackEncodedString(DFA128_eotS);
    static final short[] DFA128_eof = DFA.unpackEncodedString(DFA128_eofS);
    static final char[] DFA128_min = DFA.unpackEncodedStringToUnsignedChars(DFA128_minS);
    static final char[] DFA128_max = DFA.unpackEncodedStringToUnsignedChars(DFA128_maxS);
    static final short[] DFA128_accept = DFA.unpackEncodedString(DFA128_acceptS);
    static final short[] DFA128_special = DFA.unpackEncodedString(DFA128_specialS);
    static final short[][] DFA128_transition;

    static {
        int numStates = DFA128_transitionS.length;
        DFA128_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA128_transition[i] = DFA.unpackEncodedString(DFA128_transitionS[i]);
        }
    }

    class DFA128 extends DFA {

        public DFA128(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 128;
            this.eot = DFA128_eot;
            this.eof = DFA128_eof;
            this.min = DFA128_min;
            this.max = DFA128_max;
            this.accept = DFA128_accept;
            this.special = DFA128_special;
            this.transition = DFA128_transition;
        }
        public String getDescription() {
            return "807:1: forInit : ( ( variableModifier )* type variableDeclarators | expressionList );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA128_26 = input.LA(1);

                         
                        int index128_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred186_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index128_26);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA128_27 = input.LA(1);

                         
                        int index128_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred186_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index128_27);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA128_28 = input.LA(1);

                         
                        int index128_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred186_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index128_28);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA128_55 = input.LA(1);

                         
                        int index128_55 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred186_Java()) ) {s = 1;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index128_55);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 128, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA131_eotS =
        "\u00e5\uffff";
    static final String DFA131_eofS =
        "\1\14\u00e4\uffff";
    static final String DFA131_minS =
        "\1\31\13\0\u00d9\uffff";
    static final String DFA131_maxS =
        "\1\146\13\0\u00d9\uffff";
    static final String DFA131_acceptS =
        "\14\uffff\1\2\35\uffff\1\1\u00ba\uffff";
    static final String DFA131_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\u00d9\uffff}>";
    static final String[] DFA131_transitionS = {
            "\1\14\7\uffff\1\12\1\14\1\13\2\uffff\1\14\3\uffff\1\14\1\uffff"+
            "\1\1\25\uffff\1\14\7\uffff\1\14\24\uffff\1\2\1\3\1\4\1\5\1\6"+
            "\1\7\1\10\1\11",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA131_eot = DFA.unpackEncodedString(DFA131_eotS);
    static final short[] DFA131_eof = DFA.unpackEncodedString(DFA131_eofS);
    static final char[] DFA131_min = DFA.unpackEncodedStringToUnsignedChars(DFA131_minS);
    static final char[] DFA131_max = DFA.unpackEncodedStringToUnsignedChars(DFA131_maxS);
    static final short[] DFA131_accept = DFA.unpackEncodedString(DFA131_acceptS);
    static final short[] DFA131_special = DFA.unpackEncodedString(DFA131_specialS);
    static final short[][] DFA131_transition;

    static {
        int numStates = DFA131_transitionS.length;
        DFA131_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA131_transition[i] = DFA.unpackEncodedString(DFA131_transitionS[i]);
        }
    }

    class DFA131 extends DFA {

        public DFA131(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 131;
            this.eot = DFA131_eot;
            this.eof = DFA131_eof;
            this.min = DFA131_min;
            this.max = DFA131_max;
            this.accept = DFA131_accept;
            this.special = DFA131_special;
            this.transition = DFA131_transition;
        }
        public String getDescription() {
            return "845:26: ( assignmentOperator expression )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA131_1 = input.LA(1);

                         
                        int index131_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred189_Java()) ) {s = 42;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index131_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA131_2 = input.LA(1);

                         
                        int index131_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred189_Java()) ) {s = 42;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index131_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA131_3 = input.LA(1);

                         
                        int index131_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred189_Java()) ) {s = 42;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index131_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA131_4 = input.LA(1);

                         
                        int index131_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred189_Java()) ) {s = 42;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index131_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA131_5 = input.LA(1);

                         
                        int index131_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred189_Java()) ) {s = 42;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index131_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA131_6 = input.LA(1);

                         
                        int index131_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred189_Java()) ) {s = 42;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index131_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA131_7 = input.LA(1);

                         
                        int index131_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred189_Java()) ) {s = 42;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index131_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA131_8 = input.LA(1);

                         
                        int index131_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred189_Java()) ) {s = 42;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index131_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA131_9 = input.LA(1);

                         
                        int index131_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred189_Java()) ) {s = 42;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index131_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA131_10 = input.LA(1);

                         
                        int index131_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred189_Java()) ) {s = 42;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index131_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA131_11 = input.LA(1);

                         
                        int index131_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred189_Java()) ) {s = 42;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index131_11);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 131, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA132_eotS =
        "\17\uffff";
    static final String DFA132_eofS =
        "\17\uffff";
    static final String DFA132_minS =
        "\1\41\12\uffff\1\43\1\0\2\uffff";
    static final String DFA132_maxS =
        "\1\146\12\uffff\1\43\1\0\2\uffff";
    static final String DFA132_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\2\uffff\1\13"+
        "\1\14";
    static final String DFA132_specialS =
        "\14\uffff\1\0\2\uffff}>";
    static final String[] DFA132_transitionS = {
            "\1\12\1\uffff\1\13\10\uffff\1\1\62\uffff\1\2\1\3\1\4\1\5\1"+
            "\6\1\7\1\10\1\11",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\14",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA132_eot = DFA.unpackEncodedString(DFA132_eotS);
    static final short[] DFA132_eof = DFA.unpackEncodedString(DFA132_eofS);
    static final char[] DFA132_min = DFA.unpackEncodedStringToUnsignedChars(DFA132_minS);
    static final char[] DFA132_max = DFA.unpackEncodedStringToUnsignedChars(DFA132_maxS);
    static final short[] DFA132_accept = DFA.unpackEncodedString(DFA132_acceptS);
    static final short[] DFA132_special = DFA.unpackEncodedString(DFA132_specialS);
    static final short[][] DFA132_transition;

    static {
        int numStates = DFA132_transitionS.length;
        DFA132_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA132_transition[i] = DFA.unpackEncodedString(DFA132_transitionS[i]);
        }
    }

    class DFA132 extends DFA {

        public DFA132(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 132;
            this.eot = DFA132_eot;
            this.eof = DFA132_eof;
            this.min = DFA132_min;
            this.max = DFA132_max;
            this.accept = DFA132_accept;
            this.special = DFA132_special;
            this.transition = DFA132_transition;
        }
        public String getDescription() {
            return "848:1: assignmentOperator : ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | '<' '<' '=' | '>' '>' '=' | '>' '>' '>' '=' );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA132_12 = input.LA(1);

                         
                        int index132_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred200_Java()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index132_12);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 132, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA133_eotS =
        "\24\uffff";
    static final String DFA133_eofS =
        "\1\2\23\uffff";
    static final String DFA133_minS =
        "\1\31\23\uffff";
    static final String DFA133_maxS =
        "\1\146\23\uffff";
    static final String DFA133_acceptS =
        "\1\uffff\1\1\1\2\21\uffff";
    static final String DFA133_specialS =
        "\24\uffff}>";
    static final String[] DFA133_transitionS = {
            "\1\2\7\uffff\3\2\2\uffff\1\2\3\uffff\1\2\1\uffff\1\2\22\uffff"+
            "\1\1\2\uffff\1\2\7\uffff\1\2\24\uffff\10\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA133_eot = DFA.unpackEncodedString(DFA133_eotS);
    static final short[] DFA133_eof = DFA.unpackEncodedString(DFA133_eofS);
    static final char[] DFA133_min = DFA.unpackEncodedStringToUnsignedChars(DFA133_minS);
    static final char[] DFA133_max = DFA.unpackEncodedStringToUnsignedChars(DFA133_maxS);
    static final short[] DFA133_accept = DFA.unpackEncodedString(DFA133_acceptS);
    static final short[] DFA133_special = DFA.unpackEncodedString(DFA133_specialS);
    static final short[][] DFA133_transition;

    static {
        int numStates = DFA133_transitionS.length;
        DFA133_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA133_transition[i] = DFA.unpackEncodedString(DFA133_transitionS[i]);
        }
    }

    class DFA133 extends DFA {

        public DFA133(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 133;
            this.eot = DFA133_eot;
            this.eof = DFA133_eof;
            this.min = DFA133_min;
            this.max = DFA133_max;
            this.accept = DFA133_accept;
            this.special = DFA133_special;
            this.transition = DFA133_transition;
        }
        public String getDescription() {
            return "864:33: ( '?' expression ':' expression )?";
        }
    }
    static final String DFA134_eotS =
        "\25\uffff";
    static final String DFA134_eofS =
        "\1\1\24\uffff";
    static final String DFA134_minS =
        "\1\31\24\uffff";
    static final String DFA134_maxS =
        "\1\147\24\uffff";
    static final String DFA134_acceptS =
        "\1\uffff\1\2\22\uffff\1\1";
    static final String DFA134_specialS =
        "\25\uffff}>";
    static final String[] DFA134_transitionS = {
            "\1\1\7\uffff\3\1\2\uffff\1\1\3\uffff\1\1\1\uffff\1\1\22\uffff"+
            "\1\1\2\uffff\1\1\7\uffff\1\1\24\uffff\10\1\1\24",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA134_eot = DFA.unpackEncodedString(DFA134_eotS);
    static final short[] DFA134_eof = DFA.unpackEncodedString(DFA134_eofS);
    static final char[] DFA134_min = DFA.unpackEncodedStringToUnsignedChars(DFA134_minS);
    static final char[] DFA134_max = DFA.unpackEncodedStringToUnsignedChars(DFA134_maxS);
    static final short[] DFA134_accept = DFA.unpackEncodedString(DFA134_acceptS);
    static final short[] DFA134_special = DFA.unpackEncodedString(DFA134_specialS);
    static final short[][] DFA134_transition;

    static {
        int numStates = DFA134_transitionS.length;
        DFA134_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA134_transition[i] = DFA.unpackEncodedString(DFA134_transitionS[i]);
        }
    }

    class DFA134 extends DFA {

        public DFA134(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 134;
            this.eot = DFA134_eot;
            this.eof = DFA134_eof;
            this.min = DFA134_min;
            this.max = DFA134_max;
            this.accept = DFA134_accept;
            this.special = DFA134_special;
            this.transition = DFA134_transition;
        }
        public String getDescription() {
            return "()* loopback of 868:34: ( '||' conditionalAndExpression )*";
        }
    }
    static final String DFA135_eotS =
        "\26\uffff";
    static final String DFA135_eofS =
        "\1\1\25\uffff";
    static final String DFA135_minS =
        "\1\31\25\uffff";
    static final String DFA135_maxS =
        "\1\150\25\uffff";
    static final String DFA135_acceptS =
        "\1\uffff\1\2\23\uffff\1\1";
    static final String DFA135_specialS =
        "\26\uffff}>";
    static final String[] DFA135_transitionS = {
            "\1\1\7\uffff\3\1\2\uffff\1\1\3\uffff\1\1\1\uffff\1\1\22\uffff"+
            "\1\1\2\uffff\1\1\7\uffff\1\1\24\uffff\11\1\1\25",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA135_eot = DFA.unpackEncodedString(DFA135_eotS);
    static final short[] DFA135_eof = DFA.unpackEncodedString(DFA135_eofS);
    static final char[] DFA135_min = DFA.unpackEncodedStringToUnsignedChars(DFA135_minS);
    static final char[] DFA135_max = DFA.unpackEncodedStringToUnsignedChars(DFA135_maxS);
    static final short[] DFA135_accept = DFA.unpackEncodedString(DFA135_acceptS);
    static final short[] DFA135_special = DFA.unpackEncodedString(DFA135_specialS);
    static final short[][] DFA135_transition;

    static {
        int numStates = DFA135_transitionS.length;
        DFA135_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA135_transition[i] = DFA.unpackEncodedString(DFA135_transitionS[i]);
        }
    }

    class DFA135 extends DFA {

        public DFA135(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 135;
            this.eot = DFA135_eot;
            this.eof = DFA135_eof;
            this.min = DFA135_min;
            this.max = DFA135_max;
            this.accept = DFA135_accept;
            this.special = DFA135_special;
            this.transition = DFA135_transition;
        }
        public String getDescription() {
            return "()* loopback of 872:31: ( '&&' inclusiveOrExpression )*";
        }
    }
    static final String DFA136_eotS =
        "\27\uffff";
    static final String DFA136_eofS =
        "\1\1\26\uffff";
    static final String DFA136_minS =
        "\1\31\26\uffff";
    static final String DFA136_maxS =
        "\1\151\26\uffff";
    static final String DFA136_acceptS =
        "\1\uffff\1\2\24\uffff\1\1";
    static final String DFA136_specialS =
        "\27\uffff}>";
    static final String[] DFA136_transitionS = {
            "\1\1\7\uffff\3\1\2\uffff\1\1\3\uffff\1\1\1\uffff\1\1\22\uffff"+
            "\1\1\2\uffff\1\1\7\uffff\1\1\24\uffff\12\1\1\26",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA136_eot = DFA.unpackEncodedString(DFA136_eotS);
    static final short[] DFA136_eof = DFA.unpackEncodedString(DFA136_eofS);
    static final char[] DFA136_min = DFA.unpackEncodedStringToUnsignedChars(DFA136_minS);
    static final char[] DFA136_max = DFA.unpackEncodedStringToUnsignedChars(DFA136_maxS);
    static final short[] DFA136_accept = DFA.unpackEncodedString(DFA136_acceptS);
    static final short[] DFA136_special = DFA.unpackEncodedString(DFA136_specialS);
    static final short[][] DFA136_transition;

    static {
        int numStates = DFA136_transitionS.length;
        DFA136_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA136_transition[i] = DFA.unpackEncodedString(DFA136_transitionS[i]);
        }
    }

    class DFA136 extends DFA {

        public DFA136(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 136;
            this.eot = DFA136_eot;
            this.eof = DFA136_eof;
            this.min = DFA136_min;
            this.max = DFA136_max;
            this.accept = DFA136_accept;
            this.special = DFA136_special;
            this.transition = DFA136_transition;
        }
        public String getDescription() {
            return "()* loopback of 876:31: ( '|' exclusiveOrExpression )*";
        }
    }
    static final String DFA137_eotS =
        "\30\uffff";
    static final String DFA137_eofS =
        "\1\1\27\uffff";
    static final String DFA137_minS =
        "\1\31\27\uffff";
    static final String DFA137_maxS =
        "\1\152\27\uffff";
    static final String DFA137_acceptS =
        "\1\uffff\1\2\25\uffff\1\1";
    static final String DFA137_specialS =
        "\30\uffff}>";
    static final String[] DFA137_transitionS = {
            "\1\1\7\uffff\3\1\2\uffff\1\1\3\uffff\1\1\1\uffff\1\1\22\uffff"+
            "\1\1\2\uffff\1\1\7\uffff\1\1\24\uffff\13\1\1\27",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA137_eot = DFA.unpackEncodedString(DFA137_eotS);
    static final short[] DFA137_eof = DFA.unpackEncodedString(DFA137_eofS);
    static final char[] DFA137_min = DFA.unpackEncodedStringToUnsignedChars(DFA137_minS);
    static final char[] DFA137_max = DFA.unpackEncodedStringToUnsignedChars(DFA137_maxS);
    static final short[] DFA137_accept = DFA.unpackEncodedString(DFA137_acceptS);
    static final short[] DFA137_special = DFA.unpackEncodedString(DFA137_specialS);
    static final short[][] DFA137_transition;

    static {
        int numStates = DFA137_transitionS.length;
        DFA137_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA137_transition[i] = DFA.unpackEncodedString(DFA137_transitionS[i]);
        }
    }

    class DFA137 extends DFA {

        public DFA137(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 137;
            this.eot = DFA137_eot;
            this.eof = DFA137_eof;
            this.min = DFA137_min;
            this.max = DFA137_max;
            this.accept = DFA137_accept;
            this.special = DFA137_special;
            this.transition = DFA137_transition;
        }
        public String getDescription() {
            return "()* loopback of 880:23: ( '^' andExpression )*";
        }
    }
    static final String DFA138_eotS =
        "\31\uffff";
    static final String DFA138_eofS =
        "\1\1\30\uffff";
    static final String DFA138_minS =
        "\1\31\30\uffff";
    static final String DFA138_maxS =
        "\1\152\30\uffff";
    static final String DFA138_acceptS =
        "\1\uffff\1\2\26\uffff\1\1";
    static final String DFA138_specialS =
        "\31\uffff}>";
    static final String[] DFA138_transitionS = {
            "\1\1\7\uffff\3\1\1\30\1\uffff\1\1\3\uffff\1\1\1\uffff\1\1\22"+
            "\uffff\1\1\2\uffff\1\1\7\uffff\1\1\24\uffff\14\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA138_eot = DFA.unpackEncodedString(DFA138_eotS);
    static final short[] DFA138_eof = DFA.unpackEncodedString(DFA138_eofS);
    static final char[] DFA138_min = DFA.unpackEncodedStringToUnsignedChars(DFA138_minS);
    static final char[] DFA138_max = DFA.unpackEncodedStringToUnsignedChars(DFA138_maxS);
    static final short[] DFA138_accept = DFA.unpackEncodedString(DFA138_acceptS);
    static final short[] DFA138_special = DFA.unpackEncodedString(DFA138_specialS);
    static final short[][] DFA138_transition;

    static {
        int numStates = DFA138_transitionS.length;
        DFA138_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA138_transition[i] = DFA.unpackEncodedString(DFA138_transitionS[i]);
        }
    }

    class DFA138 extends DFA {

        public DFA138(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 138;
            this.eot = DFA138_eot;
            this.eof = DFA138_eof;
            this.min = DFA138_min;
            this.max = DFA138_max;
            this.accept = DFA138_accept;
            this.special = DFA138_special;
            this.transition = DFA138_transition;
        }
        public String getDescription() {
            return "()* loopback of 884:28: ( '&' equalityExpression )*";
        }
    }
    static final String DFA139_eotS =
        "\32\uffff";
    static final String DFA139_eofS =
        "\1\1\31\uffff";
    static final String DFA139_minS =
        "\1\31\31\uffff";
    static final String DFA139_maxS =
        "\1\154\31\uffff";
    static final String DFA139_acceptS =
        "\1\uffff\1\2\27\uffff\1\1";
    static final String DFA139_specialS =
        "\32\uffff}>";
    static final String[] DFA139_transitionS = {
            "\1\1\7\uffff\4\1\1\uffff\1\1\3\uffff\1\1\1\uffff\1\1\22\uffff"+
            "\1\1\2\uffff\1\1\7\uffff\1\1\24\uffff\14\1\2\31",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA139_eot = DFA.unpackEncodedString(DFA139_eotS);
    static final short[] DFA139_eof = DFA.unpackEncodedString(DFA139_eofS);
    static final char[] DFA139_min = DFA.unpackEncodedStringToUnsignedChars(DFA139_minS);
    static final char[] DFA139_max = DFA.unpackEncodedStringToUnsignedChars(DFA139_maxS);
    static final short[] DFA139_accept = DFA.unpackEncodedString(DFA139_acceptS);
    static final short[] DFA139_special = DFA.unpackEncodedString(DFA139_specialS);
    static final short[][] DFA139_transition;

    static {
        int numStates = DFA139_transitionS.length;
        DFA139_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA139_transition[i] = DFA.unpackEncodedString(DFA139_transitionS[i]);
        }
    }

    class DFA139 extends DFA {

        public DFA139(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 139;
            this.eot = DFA139_eot;
            this.eof = DFA139_eof;
            this.min = DFA139_min;
            this.max = DFA139_max;
            this.accept = DFA139_accept;
            this.special = DFA139_special;
            this.transition = DFA139_transition;
        }
        public String getDescription() {
            return "()* loopback of 888:30: ( ( '==' | '!=' ) instanceOfExpression )*";
        }
    }
    static final String DFA140_eotS =
        "\33\uffff";
    static final String DFA140_eofS =
        "\1\2\32\uffff";
    static final String DFA140_minS =
        "\1\31\32\uffff";
    static final String DFA140_maxS =
        "\1\155\32\uffff";
    static final String DFA140_acceptS =
        "\1\uffff\1\1\1\2\30\uffff";
    static final String DFA140_specialS =
        "\33\uffff}>";
    static final String[] DFA140_transitionS = {
            "\1\2\7\uffff\4\2\1\uffff\1\2\3\uffff\1\2\1\uffff\1\2\22\uffff"+
            "\1\2\2\uffff\1\2\7\uffff\1\2\24\uffff\16\2\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA140_eot = DFA.unpackEncodedString(DFA140_eotS);
    static final short[] DFA140_eof = DFA.unpackEncodedString(DFA140_eofS);
    static final char[] DFA140_min = DFA.unpackEncodedStringToUnsignedChars(DFA140_minS);
    static final char[] DFA140_max = DFA.unpackEncodedStringToUnsignedChars(DFA140_maxS);
    static final short[] DFA140_accept = DFA.unpackEncodedString(DFA140_acceptS);
    static final short[] DFA140_special = DFA.unpackEncodedString(DFA140_specialS);
    static final short[][] DFA140_transition;

    static {
        int numStates = DFA140_transitionS.length;
        DFA140_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA140_transition[i] = DFA.unpackEncodedString(DFA140_transitionS[i]);
        }
    }

    class DFA140 extends DFA {

        public DFA140(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 140;
            this.eot = DFA140_eot;
            this.eof = DFA140_eof;
            this.min = DFA140_min;
            this.max = DFA140_max;
            this.accept = DFA140_accept;
            this.special = DFA140_special;
            this.transition = DFA140_transition;
        }
        public String getDescription() {
            return "892:30: ( 'instanceof' type )?";
        }
    }
    static final String DFA141_eotS =
        "\114\uffff";
    static final String DFA141_eofS =
        "\1\1\113\uffff";
    static final String DFA141_minS =
        "\1\31\26\uffff\2\4\2\uffff\1\0\60\uffff";
    static final String DFA141_maxS =
        "\1\155\26\uffff\2\167\2\uffff\1\0\60\uffff";
    static final String DFA141_acceptS =
        "\1\uffff\1\2\32\uffff\1\1\57\uffff";
    static final String DFA141_specialS =
        "\33\uffff\1\0\60\uffff}>";
    static final String[] DFA141_transitionS = {
            "\1\1\7\uffff\1\27\1\1\1\30\1\1\1\uffff\1\1\3\uffff\1\1\1\uffff"+
            "\1\1\22\uffff\1\1\2\uffff\1\1\7\uffff\1\1\24\uffff\17\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\34\1\uffff\6\34\25\uffff\1\33\6\uffff\1\34\3\uffff\1\34"+
            "\12\uffff\10\34\1\uffff\2\34\2\uffff\3\34\23\uffff\3\34\21\uffff"+
            "\2\34\2\uffff\6\34",
            "\1\34\1\uffff\6\34\25\uffff\1\34\1\uffff\1\1\4\uffff\1\34"+
            "\3\uffff\1\34\12\uffff\10\34\1\uffff\2\34\2\uffff\3\34\23\uffff"+
            "\3\34\21\uffff\2\34\2\uffff\6\34",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA141_eot = DFA.unpackEncodedString(DFA141_eotS);
    static final short[] DFA141_eof = DFA.unpackEncodedString(DFA141_eofS);
    static final char[] DFA141_min = DFA.unpackEncodedStringToUnsignedChars(DFA141_minS);
    static final char[] DFA141_max = DFA.unpackEncodedStringToUnsignedChars(DFA141_maxS);
    static final short[] DFA141_accept = DFA.unpackEncodedString(DFA141_acceptS);
    static final short[] DFA141_special = DFA.unpackEncodedString(DFA141_specialS);
    static final short[][] DFA141_transition;

    static {
        int numStates = DFA141_transitionS.length;
        DFA141_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA141_transition[i] = DFA.unpackEncodedString(DFA141_transitionS[i]);
        }
    }

    class DFA141 extends DFA {

        public DFA141(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 141;
            this.eot = DFA141_eot;
            this.eof = DFA141_eof;
            this.min = DFA141_min;
            this.max = DFA141_max;
            this.accept = DFA141_accept;
            this.special = DFA141_special;
            this.transition = DFA141_transition;
        }
        public String getDescription() {
            return "()* loopback of 896:25: ( relationalOp shiftExpression )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA141_27 = input.LA(1);

                         
                        int index141_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred210_Java()) ) {s = 28;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index141_27);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 141, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA142_eotS =
        "\63\uffff";
    static final String DFA142_eofS =
        "\63\uffff";
    static final String DFA142_minS =
        "\1\41\2\4\60\uffff";
    static final String DFA142_maxS =
        "\1\43\2\167\60\uffff";
    static final String DFA142_acceptS =
        "\3\uffff\1\1\1\3\26\uffff\1\2\1\4\26\uffff";
    static final String DFA142_specialS =
        "\63\uffff}>";
    static final String[] DFA142_transitionS = {
            "\1\1\1\uffff\1\2",
            "\1\4\1\uffff\6\4\25\uffff\1\4\6\uffff\1\4\3\uffff\1\3\12\uffff"+
            "\10\4\1\uffff\2\4\2\uffff\3\4\23\uffff\3\4\21\uffff\2\4\2\uffff"+
            "\6\4",
            "\1\34\1\uffff\6\34\25\uffff\1\34\6\uffff\1\34\3\uffff\1\33"+
            "\12\uffff\10\34\1\uffff\2\34\2\uffff\3\34\23\uffff\3\34\21\uffff"+
            "\2\34\2\uffff\6\34",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA142_eot = DFA.unpackEncodedString(DFA142_eotS);
    static final short[] DFA142_eof = DFA.unpackEncodedString(DFA142_eofS);
    static final char[] DFA142_min = DFA.unpackEncodedStringToUnsignedChars(DFA142_minS);
    static final char[] DFA142_max = DFA.unpackEncodedStringToUnsignedChars(DFA142_maxS);
    static final short[] DFA142_accept = DFA.unpackEncodedString(DFA142_acceptS);
    static final short[] DFA142_special = DFA.unpackEncodedString(DFA142_specialS);
    static final short[][] DFA142_transition;

    static {
        int numStates = DFA142_transitionS.length;
        DFA142_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA142_transition[i] = DFA.unpackEncodedString(DFA142_transitionS[i]);
        }
    }

    class DFA142 extends DFA {

        public DFA142(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 142;
            this.eot = DFA142_eot;
            this.eof = DFA142_eof;
            this.min = DFA142_min;
            this.max = DFA142_max;
            this.accept = DFA142_accept;
            this.special = DFA142_special;
            this.transition = DFA142_transition;
        }
        public String getDescription() {
            return "900:4: ( '<' '=' | '>' '=' | '<' | '>' )";
        }
    }
    static final String DFA143_eotS =
        "\115\uffff";
    static final String DFA143_eofS =
        "\1\3\114\uffff";
    static final String DFA143_minS =
        "\1\31\2\4\31\uffff\1\0\27\uffff\1\0\30\uffff";
    static final String DFA143_maxS =
        "\1\155\2\167\31\uffff\1\0\27\uffff\1\0\30\uffff";
    static final String DFA143_acceptS =
        "\3\uffff\1\2\110\uffff\1\1";
    static final String DFA143_specialS =
        "\34\uffff\1\0\27\uffff\1\1\30\uffff}>";
    static final String[] DFA143_transitionS = {
            "\1\3\7\uffff\1\1\1\3\1\2\1\3\1\uffff\1\3\3\uffff\1\3\1\uffff"+
            "\1\3\22\uffff\1\3\2\uffff\1\3\7\uffff\1\3\24\uffff\17\3",
            "\1\3\1\uffff\6\3\25\uffff\1\34\6\uffff\1\3\3\uffff\1\3\12"+
            "\uffff\10\3\1\uffff\2\3\2\uffff\3\3\23\uffff\3\3\21\uffff\2"+
            "\3\2\uffff\6\3",
            "\1\3\1\uffff\6\3\25\uffff\1\3\1\uffff\1\64\4\uffff\1\3\3\uffff"+
            "\1\3\12\uffff\10\3\1\uffff\2\3\2\uffff\3\3\23\uffff\3\3\21\uffff"+
            "\2\3\2\uffff\6\3",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA143_eot = DFA.unpackEncodedString(DFA143_eotS);
    static final short[] DFA143_eof = DFA.unpackEncodedString(DFA143_eofS);
    static final char[] DFA143_min = DFA.unpackEncodedStringToUnsignedChars(DFA143_minS);
    static final char[] DFA143_max = DFA.unpackEncodedStringToUnsignedChars(DFA143_maxS);
    static final short[] DFA143_accept = DFA.unpackEncodedString(DFA143_acceptS);
    static final short[] DFA143_special = DFA.unpackEncodedString(DFA143_specialS);
    static final short[][] DFA143_transition;

    static {
        int numStates = DFA143_transitionS.length;
        DFA143_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA143_transition[i] = DFA.unpackEncodedString(DFA143_transitionS[i]);
        }
    }

    class DFA143 extends DFA {

        public DFA143(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 143;
            this.eot = DFA143_eot;
            this.eof = DFA143_eof;
            this.min = DFA143_min;
            this.max = DFA143_max;
            this.accept = DFA143_accept;
            this.special = DFA143_special;
            this.transition = DFA143_transition;
        }
        public String getDescription() {
            return "()* loopback of 904:28: ( shiftOp additiveExpression )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA143_28 = input.LA(1);

                         
                        int index143_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred214_Java()) ) {s = 76;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index143_28);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA143_52 = input.LA(1);

                         
                        int index143_52 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred214_Java()) ) {s = 76;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index143_52);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 143, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA145_eotS =
        "\34\uffff";
    static final String DFA145_eofS =
        "\1\1\33\uffff";
    static final String DFA145_minS =
        "\1\31\33\uffff";
    static final String DFA145_maxS =
        "\1\157\33\uffff";
    static final String DFA145_acceptS =
        "\1\uffff\1\2\31\uffff\1\1";
    static final String DFA145_specialS =
        "\34\uffff}>";
    static final String[] DFA145_transitionS = {
            "\1\1\7\uffff\4\1\1\uffff\1\1\3\uffff\1\1\1\uffff\1\1\22\uffff"+
            "\1\1\2\uffff\1\1\7\uffff\1\1\24\uffff\17\1\2\33",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA145_eot = DFA.unpackEncodedString(DFA145_eotS);
    static final short[] DFA145_eof = DFA.unpackEncodedString(DFA145_eofS);
    static final char[] DFA145_min = DFA.unpackEncodedStringToUnsignedChars(DFA145_minS);
    static final char[] DFA145_max = DFA.unpackEncodedStringToUnsignedChars(DFA145_maxS);
    static final short[] DFA145_accept = DFA.unpackEncodedString(DFA145_acceptS);
    static final short[] DFA145_special = DFA.unpackEncodedString(DFA145_specialS);
    static final short[][] DFA145_transition;

    static {
        int numStates = DFA145_transitionS.length;
        DFA145_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA145_transition[i] = DFA.unpackEncodedString(DFA145_transitionS[i]);
        }
    }

    class DFA145 extends DFA {

        public DFA145(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 145;
            this.eot = DFA145_eot;
            this.eof = DFA145_eof;
            this.min = DFA145_min;
            this.max = DFA145_max;
            this.accept = DFA145_accept;
            this.special = DFA145_special;
            this.transition = DFA145_transition;
        }
        public String getDescription() {
            return "()* loopback of 914:34: ( ( '+' | '-' ) multiplicativeExpression )*";
        }
    }
    static final String DFA146_eotS =
        "\35\uffff";
    static final String DFA146_eofS =
        "\1\1\34\uffff";
    static final String DFA146_minS =
        "\1\31\34\uffff";
    static final String DFA146_maxS =
        "\1\161\34\uffff";
    static final String DFA146_acceptS =
        "\1\uffff\1\2\32\uffff\1\1";
    static final String DFA146_specialS =
        "\35\uffff}>";
    static final String[] DFA146_transitionS = {
            "\1\1\3\uffff\1\34\3\uffff\4\1\1\uffff\1\1\3\uffff\1\1\1\uffff"+
            "\1\1\22\uffff\1\1\2\uffff\1\1\7\uffff\1\1\24\uffff\21\1\2\34",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA146_eot = DFA.unpackEncodedString(DFA146_eotS);
    static final short[] DFA146_eof = DFA.unpackEncodedString(DFA146_eofS);
    static final char[] DFA146_min = DFA.unpackEncodedStringToUnsignedChars(DFA146_minS);
    static final char[] DFA146_max = DFA.unpackEncodedStringToUnsignedChars(DFA146_maxS);
    static final short[] DFA146_accept = DFA.unpackEncodedString(DFA146_acceptS);
    static final short[] DFA146_special = DFA.unpackEncodedString(DFA146_specialS);
    static final short[][] DFA146_transition;

    static {
        int numStates = DFA146_transitionS.length;
        DFA146_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA146_transition[i] = DFA.unpackEncodedString(DFA146_transitionS[i]);
        }
    }

    class DFA146 extends DFA {

        public DFA146(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 146;
            this.eot = DFA146_eot;
            this.eof = DFA146_eof;
            this.min = DFA146_min;
            this.max = DFA146_max;
            this.accept = DFA146_accept;
            this.special = DFA146_special;
            this.transition = DFA146_transition;
        }
        public String getDescription() {
            return "()* loopback of 918:25: ( ( '*' | '/' | '%' ) unaryExpression )*";
        }
    }
    static final String DFA147_eotS =
        "\30\uffff";
    static final String DFA147_eofS =
        "\30\uffff";
    static final String DFA147_minS =
        "\1\4\27\uffff";
    static final String DFA147_maxS =
        "\1\167\27\uffff";
    static final String DFA147_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\22\uffff";
    static final String DFA147_specialS =
        "\30\uffff}>";
    static final String[] DFA147_transitionS = {
            "\1\5\1\uffff\6\5\25\uffff\1\5\6\uffff\1\5\16\uffff\10\5\1\uffff"+
            "\2\5\2\uffff\3\5\23\uffff\3\5\21\uffff\1\1\1\2\2\uffff\1\3\1"+
            "\4\4\5",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA147_eot = DFA.unpackEncodedString(DFA147_eotS);
    static final short[] DFA147_eof = DFA.unpackEncodedString(DFA147_eofS);
    static final char[] DFA147_min = DFA.unpackEncodedStringToUnsignedChars(DFA147_minS);
    static final char[] DFA147_max = DFA.unpackEncodedStringToUnsignedChars(DFA147_maxS);
    static final short[] DFA147_accept = DFA.unpackEncodedString(DFA147_acceptS);
    static final short[] DFA147_special = DFA.unpackEncodedString(DFA147_specialS);
    static final short[][] DFA147_transition;

    static {
        int numStates = DFA147_transitionS.length;
        DFA147_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA147_transition[i] = DFA.unpackEncodedString(DFA147_transitionS[i]);
        }
    }

    class DFA147 extends DFA {

        public DFA147(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 147;
            this.eot = DFA147_eot;
            this.eof = DFA147_eof;
            this.min = DFA147_min;
            this.max = DFA147_max;
            this.accept = DFA147_accept;
            this.special = DFA147_special;
            this.transition = DFA147_transition;
        }
        public String getDescription() {
            return "921:1: unaryExpression : ( '+' unaryExpression | '-' unaryExpression | '++' primary | '--' primary | unaryExpressionNotPlusMinus );";
        }
    }
    static final String DFA150_eotS =
        "\54\uffff";
    static final String DFA150_eofS =
        "\54\uffff";
    static final String DFA150_minS =
        "\1\4\2\uffff\1\4\20\uffff\27\0\1\uffff";
    static final String DFA150_maxS =
        "\1\167\2\uffff\1\167\20\uffff\27\0\1\uffff";
    static final String DFA150_acceptS =
        "\1\uffff\1\1\1\2\1\uffff\1\4\46\uffff\1\3";
    static final String DFA150_specialS =
        "\24\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\uffff}>";
    static final String[] DFA150_transitionS = {
            "\1\4\1\uffff\6\4\25\uffff\1\4\6\uffff\1\4\16\uffff\10\4\1\uffff"+
            "\1\4\1\3\2\uffff\3\4\23\uffff\3\4\27\uffff\1\1\1\2\2\4",
            "",
            "",
            "\1\25\1\uffff\1\44\1\45\1\46\3\43\25\uffff\1\35\6\uffff\1"+
            "\52\16\uffff\10\24\1\uffff\1\37\1\34\2\uffff\1\50\2\47\23\uffff"+
            "\1\40\1\41\1\42\21\uffff\1\26\1\27\2\uffff\1\30\1\31\1\32\1"+
            "\33\1\36\1\51",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            ""
    };

    static final short[] DFA150_eot = DFA.unpackEncodedString(DFA150_eotS);
    static final short[] DFA150_eof = DFA.unpackEncodedString(DFA150_eofS);
    static final char[] DFA150_min = DFA.unpackEncodedStringToUnsignedChars(DFA150_minS);
    static final char[] DFA150_max = DFA.unpackEncodedStringToUnsignedChars(DFA150_maxS);
    static final short[] DFA150_accept = DFA.unpackEncodedString(DFA150_acceptS);
    static final short[] DFA150_special = DFA.unpackEncodedString(DFA150_specialS);
    static final short[][] DFA150_transition;

    static {
        int numStates = DFA150_transitionS.length;
        DFA150_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA150_transition[i] = DFA.unpackEncodedString(DFA150_transitionS[i]);
        }
    }

    class DFA150 extends DFA {

        public DFA150(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 150;
            this.eot = DFA150_eot;
            this.eof = DFA150_eof;
            this.min = DFA150_min;
            this.max = DFA150_max;
            this.accept = DFA150_accept;
            this.special = DFA150_special;
            this.transition = DFA150_transition;
        }
        public String getDescription() {
            return "929:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA150_20 = input.LA(1);

                         
                        int index150_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_20);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA150_21 = input.LA(1);

                         
                        int index150_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_21);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA150_22 = input.LA(1);

                         
                        int index150_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_22);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA150_23 = input.LA(1);

                         
                        int index150_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_23);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA150_24 = input.LA(1);

                         
                        int index150_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_24);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA150_25 = input.LA(1);

                         
                        int index150_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_25);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA150_26 = input.LA(1);

                         
                        int index150_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_26);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA150_27 = input.LA(1);

                         
                        int index150_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_27);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA150_28 = input.LA(1);

                         
                        int index150_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_28);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA150_29 = input.LA(1);

                         
                        int index150_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_29);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA150_30 = input.LA(1);

                         
                        int index150_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_30);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA150_31 = input.LA(1);

                         
                        int index150_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_31);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA150_32 = input.LA(1);

                         
                        int index150_32 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_32);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA150_33 = input.LA(1);

                         
                        int index150_33 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_33);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA150_34 = input.LA(1);

                         
                        int index150_34 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_34);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA150_35 = input.LA(1);

                         
                        int index150_35 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_35);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA150_36 = input.LA(1);

                         
                        int index150_36 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_36);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA150_37 = input.LA(1);

                         
                        int index150_37 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_37);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA150_38 = input.LA(1);

                         
                        int index150_38 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_38);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA150_39 = input.LA(1);

                         
                        int index150_39 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_39);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA150_40 = input.LA(1);

                         
                        int index150_40 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_40);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA150_41 = input.LA(1);

                         
                        int index150_41 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_41);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA150_42 = input.LA(1);

                         
                        int index150_42 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred228_Java()) ) {s = 43;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index150_42);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 150, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA148_eotS =
        "\40\uffff";
    static final String DFA148_eofS =
        "\1\1\37\uffff";
    static final String DFA148_minS =
        "\1\31\37\uffff";
    static final String DFA148_maxS =
        "\1\163\37\uffff";
    static final String DFA148_acceptS =
        "\1\uffff\1\2\34\uffff\1\1\1\uffff";
    static final String DFA148_specialS =
        "\40\uffff}>";
    static final String[] DFA148_transitionS = {
            "\1\1\2\uffff\1\36\1\1\3\uffff\4\1\1\uffff\1\1\2\uffff\1\36"+
            "\1\1\1\uffff\1\1\22\uffff\1\1\2\uffff\1\1\7\uffff\1\1\24\uffff"+
            "\25\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA148_eot = DFA.unpackEncodedString(DFA148_eotS);
    static final short[] DFA148_eof = DFA.unpackEncodedString(DFA148_eofS);
    static final char[] DFA148_min = DFA.unpackEncodedStringToUnsignedChars(DFA148_minS);
    static final char[] DFA148_max = DFA.unpackEncodedStringToUnsignedChars(DFA148_maxS);
    static final short[] DFA148_accept = DFA.unpackEncodedString(DFA148_acceptS);
    static final short[] DFA148_special = DFA.unpackEncodedString(DFA148_specialS);
    static final short[][] DFA148_transition;

    static {
        int numStates = DFA148_transitionS.length;
        DFA148_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA148_transition[i] = DFA.unpackEncodedString(DFA148_transitionS[i]);
        }
    }

    class DFA148 extends DFA {

        public DFA148(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 148;
            this.eot = DFA148_eot;
            this.eof = DFA148_eof;
            this.min = DFA148_min;
            this.max = DFA148_max;
            this.accept = DFA148_accept;
            this.special = DFA148_special;
            this.transition = DFA148_transition;
        }
        public String getDescription() {
            return "()* loopback of 933:17: ( selector )*";
        }
    }
    static final String DFA149_eotS =
        "\36\uffff";
    static final String DFA149_eofS =
        "\1\2\35\uffff";
    static final String DFA149_minS =
        "\1\31\35\uffff";
    static final String DFA149_maxS =
        "\1\163\35\uffff";
    static final String DFA149_acceptS =
        "\1\uffff\1\1\1\2\33\uffff";
    static final String DFA149_specialS =
        "\36\uffff}>";
    static final String[] DFA149_transitionS = {
            "\1\2\3\uffff\1\2\3\uffff\4\2\1\uffff\1\2\3\uffff\1\2\1\uffff"+
            "\1\2\22\uffff\1\2\2\uffff\1\2\7\uffff\1\2\24\uffff\23\2\2\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA149_eot = DFA.unpackEncodedString(DFA149_eotS);
    static final short[] DFA149_eof = DFA.unpackEncodedString(DFA149_eofS);
    static final char[] DFA149_min = DFA.unpackEncodedStringToUnsignedChars(DFA149_minS);
    static final char[] DFA149_max = DFA.unpackEncodedStringToUnsignedChars(DFA149_maxS);
    static final short[] DFA149_accept = DFA.unpackEncodedString(DFA149_acceptS);
    static final short[] DFA149_special = DFA.unpackEncodedString(DFA149_specialS);
    static final short[][] DFA149_transition;

    static {
        int numStates = DFA149_transitionS.length;
        DFA149_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA149_transition[i] = DFA.unpackEncodedString(DFA149_transitionS[i]);
        }
    }

    class DFA149 extends DFA {

        public DFA149(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 149;
            this.eot = DFA149_eot;
            this.eof = DFA149_eof;
            this.min = DFA149_min;
            this.max = DFA149_max;
            this.accept = DFA149_accept;
            this.special = DFA149_special;
            this.transition = DFA149_transition;
        }
        public String getDescription() {
            return "933:27: ( '++' | '--' )?";
        }
    }
    static final String DFA152_eotS =
        "\32\uffff";
    static final String DFA152_eofS =
        "\32\uffff";
    static final String DFA152_minS =
        "\1\101\1\4\1\0\27\uffff";
    static final String DFA152_maxS =
        "\1\101\1\167\1\0\27\uffff";
    static final String DFA152_acceptS =
        "\3\uffff\1\2\25\uffff\1\1";
    static final String DFA152_specialS =
        "\2\uffff\1\0\27\uffff}>";
    static final String[] DFA152_transitionS = {
            "\1\1",
            "\1\3\1\uffff\6\3\25\uffff\1\3\6\uffff\1\3\16\uffff\10\2\1"+
            "\uffff\2\3\2\uffff\3\3\23\uffff\3\3\21\uffff\2\3\2\uffff\6\3",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA152_eot = DFA.unpackEncodedString(DFA152_eotS);
    static final short[] DFA152_eof = DFA.unpackEncodedString(DFA152_eofS);
    static final char[] DFA152_min = DFA.unpackEncodedStringToUnsignedChars(DFA152_minS);
    static final char[] DFA152_max = DFA.unpackEncodedStringToUnsignedChars(DFA152_maxS);
    static final short[] DFA152_accept = DFA.unpackEncodedString(DFA152_acceptS);
    static final short[] DFA152_special = DFA.unpackEncodedString(DFA152_specialS);
    static final short[][] DFA152_transition;

    static {
        int numStates = DFA152_transitionS.length;
        DFA152_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA152_transition[i] = DFA.unpackEncodedString(DFA152_transitionS[i]);
        }
    }

    class DFA152 extends DFA {

        public DFA152(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 152;
            this.eot = DFA152_eot;
            this.eof = DFA152_eof;
            this.min = DFA152_min;
            this.max = DFA152_max;
            this.accept = DFA152_accept;
            this.special = DFA152_special;
            this.transition = DFA152_transition;
        }
        public String getDescription() {
            return "936:1: castExpression : ( '(' primitiveType ')' unaryExpression | '(' ( type | expression ) ')' unaryExpressionNotPlusMinus );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA152_2 = input.LA(1);

                         
                        int index152_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred232_Java()) ) {s = 25;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index152_2);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 152, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA151_eotS =
        "\66\uffff";
    static final String DFA151_eofS =
        "\66\uffff";
    static final String DFA151_minS =
        "\1\4\1\0\1\34\60\uffff\1\0\2\uffff";
    static final String DFA151_maxS =
        "\1\167\1\0\1\102\60\uffff\1\0\2\uffff";
    static final String DFA151_acceptS =
        "\3\uffff\1\2\56\uffff\1\1\3\uffff";
    static final String DFA151_specialS =
        "\1\uffff\1\0\61\uffff\1\1\2\uffff}>";
    static final String[] DFA151_transitionS = {
            "\1\1\1\uffff\6\3\25\uffff\1\3\6\uffff\1\3\16\uffff\10\2\1\uffff"+
            "\2\3\2\uffff\3\3\23\uffff\3\3\21\uffff\2\3\2\uffff\6\3",
            "\1\uffff",
            "\1\3\14\uffff\1\63\30\uffff\1\62",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA151_eot = DFA.unpackEncodedString(DFA151_eotS);
    static final short[] DFA151_eof = DFA.unpackEncodedString(DFA151_eofS);
    static final char[] DFA151_min = DFA.unpackEncodedStringToUnsignedChars(DFA151_minS);
    static final char[] DFA151_max = DFA.unpackEncodedStringToUnsignedChars(DFA151_maxS);
    static final short[] DFA151_accept = DFA.unpackEncodedString(DFA151_acceptS);
    static final short[] DFA151_special = DFA.unpackEncodedString(DFA151_specialS);
    static final short[][] DFA151_transition;

    static {
        int numStates = DFA151_transitionS.length;
        DFA151_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA151_transition[i] = DFA.unpackEncodedString(DFA151_transitionS[i]);
        }
    }

    class DFA151 extends DFA {

        public DFA151(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 151;
            this.eot = DFA151_eot;
            this.eof = DFA151_eof;
            this.min = DFA151_min;
            this.max = DFA151_max;
            this.accept = DFA151_accept;
            this.special = DFA151_special;
            this.transition = DFA151_transition;
        }
        public String getDescription() {
            return "938:12: ( type | expression )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA151_1 = input.LA(1);

                         
                        int index151_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred233_Java()) ) {s = 50;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index151_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA151_51 = input.LA(1);

                         
                        int index151_51 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred233_Java()) ) {s = 50;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index151_51);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 151, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA161_eotS =
        "\22\uffff";
    static final String DFA161_eofS =
        "\22\uffff";
    static final String DFA161_minS =
        "\1\4\21\uffff";
    static final String DFA161_maxS =
        "\1\167\21\uffff";
    static final String DFA161_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\2\uffff\1\6\5\uffff\1\7\1\10\1\11"+
        "\1\12";
    static final String DFA161_specialS =
        "\22\uffff}>";
    static final String[] DFA161_transitionS = {
            "\1\17\1\uffff\6\10\25\uffff\1\2\6\uffff\1\21\16\uffff\10\20"+
            "\1\uffff\1\4\1\1\2\uffff\3\10\23\uffff\3\5\31\uffff\1\3\1\16",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA161_eot = DFA.unpackEncodedString(DFA161_eotS);
    static final short[] DFA161_eof = DFA.unpackEncodedString(DFA161_eofS);
    static final char[] DFA161_min = DFA.unpackEncodedStringToUnsignedChars(DFA161_minS);
    static final char[] DFA161_max = DFA.unpackEncodedStringToUnsignedChars(DFA161_maxS);
    static final short[] DFA161_accept = DFA.unpackEncodedString(DFA161_acceptS);
    static final short[] DFA161_special = DFA.unpackEncodedString(DFA161_specialS);
    static final short[][] DFA161_transition;

    static {
        int numStates = DFA161_transitionS.length;
        DFA161_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA161_transition[i] = DFA.unpackEncodedString(DFA161_transitionS[i]);
        }
    }

    class DFA161 extends DFA {

        public DFA161(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 161;
            this.eot = DFA161_eot;
            this.eof = DFA161_eof;
            this.min = DFA161_min;
            this.max = DFA161_max;
            this.accept = DFA161_accept;
            this.special = DFA161_special;
            this.transition = DFA161_transition;
        }
        public String getDescription() {
            return "941:1: primary : ( parExpression | nonWildcardTypeArguments ( explicitGenericInvocationSuffix | 'this' arguments ) | 'this' ( '.' Identifier )* ( identifierSuffix )? | 'super' superSuffix | epStatement ( '.' Identifier )* ( identifierSuffix )? | literal | 'new' creator | i= Identifier ( '.' Identifier )* ( identifierSuffix )? | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' );";
        }
    }
    static final String DFA154_eotS =
        "\50\uffff";
    static final String DFA154_eofS =
        "\1\1\47\uffff";
    static final String DFA154_minS =
        "\1\31\2\uffff\1\4\41\uffff\1\0\2\uffff";
    static final String DFA154_maxS =
        "\1\163\2\uffff\1\167\41\uffff\1\0\2\uffff";
    static final String DFA154_acceptS =
        "\1\uffff\1\2\45\uffff\1\1";
    static final String DFA154_specialS =
        "\45\uffff\1\0\2\uffff}>";
    static final String[] DFA154_transitionS = {
            "\1\1\2\uffff\1\3\1\1\3\uffff\4\1\1\uffff\1\1\2\uffff\2\1\1"+
            "\uffff\1\1\22\uffff\1\1\1\uffff\2\1\7\uffff\1\1\24\uffff\25"+
            "\1",
            "",
            "",
            "\1\45\31\uffff\1\1\2\uffff\1\1\36\uffff\1\1\65\uffff\2\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA154_eot = DFA.unpackEncodedString(DFA154_eotS);
    static final short[] DFA154_eof = DFA.unpackEncodedString(DFA154_eofS);
    static final char[] DFA154_min = DFA.unpackEncodedStringToUnsignedChars(DFA154_minS);
    static final char[] DFA154_max = DFA.unpackEncodedStringToUnsignedChars(DFA154_maxS);
    static final short[] DFA154_accept = DFA.unpackEncodedString(DFA154_acceptS);
    static final short[] DFA154_special = DFA.unpackEncodedString(DFA154_specialS);
    static final short[][] DFA154_transition;

    static {
        int numStates = DFA154_transitionS.length;
        DFA154_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA154_transition[i] = DFA.unpackEncodedString(DFA154_transitionS[i]);
        }
    }

    class DFA154 extends DFA {

        public DFA154(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 154;
            this.eot = DFA154_eot;
            this.eof = DFA154_eof;
            this.min = DFA154_min;
            this.max = DFA154_max;
            this.accept = DFA154_accept;
            this.special = DFA154_special;
            this.transition = DFA154_transition;
        }
        public String getDescription() {
            return "()* loopback of 945:16: ( '.' Identifier )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA154_37 = input.LA(1);

                         
                        int index154_37 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred237_Java()) ) {s = 39;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index154_37);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 154, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA155_eotS =
        "\77\uffff";
    static final String DFA155_eofS =
        "\1\4\76\uffff";
    static final String DFA155_minS =
        "\1\31\1\4\1\uffff\1\4\36\uffff\27\0\1\uffff\3\0\2\uffff";
    static final String DFA155_maxS =
        "\1\163\1\167\1\uffff\1\167\36\uffff\27\0\1\uffff\3\0\2\uffff";
    static final String DFA155_acceptS =
        "\2\uffff\1\1\1\uffff\1\2\72\uffff";
    static final String DFA155_specialS =
        "\42\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\uffff\1"+
        "\27\1\30\1\31\2\uffff}>";
    static final String[] DFA155_transitionS = {
            "\1\4\2\uffff\1\3\1\4\3\uffff\4\4\1\uffff\1\4\2\uffff\1\1\1"+
            "\4\1\uffff\1\4\22\uffff\1\4\1\uffff\1\2\1\4\7\uffff\1\4\24\uffff"+
            "\25\4",
            "\1\66\1\uffff\1\60\1\61\1\62\3\57\25\uffff\1\51\6\uffff\1"+
            "\70\1\uffff\1\2\14\uffff\10\67\1\uffff\1\53\1\50\2\uffff\1\64"+
            "\2\63\23\uffff\1\54\1\55\1\56\21\uffff\1\42\1\43\2\uffff\1\44"+
            "\1\45\1\46\1\47\1\52\1\65",
            "",
            "\1\4\31\uffff\1\2\2\uffff\1\2\36\uffff\1\73\65\uffff\1\72"+
            "\1\74",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA155_eot = DFA.unpackEncodedString(DFA155_eotS);
    static final short[] DFA155_eof = DFA.unpackEncodedString(DFA155_eofS);
    static final char[] DFA155_min = DFA.unpackEncodedStringToUnsignedChars(DFA155_minS);
    static final char[] DFA155_max = DFA.unpackEncodedStringToUnsignedChars(DFA155_maxS);
    static final short[] DFA155_accept = DFA.unpackEncodedString(DFA155_acceptS);
    static final short[] DFA155_special = DFA.unpackEncodedString(DFA155_specialS);
    static final short[][] DFA155_transition;

    static {
        int numStates = DFA155_transitionS.length;
        DFA155_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA155_transition[i] = DFA.unpackEncodedString(DFA155_transitionS[i]);
        }
    }

    class DFA155 extends DFA {

        public DFA155(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 155;
            this.eot = DFA155_eot;
            this.eof = DFA155_eof;
            this.min = DFA155_min;
            this.max = DFA155_max;
            this.accept = DFA155_accept;
            this.special = DFA155_special;
            this.transition = DFA155_transition;
        }
        public String getDescription() {
            return "945:34: ( identifierSuffix )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA155_34 = input.LA(1);

                         
                        int index155_34 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_34);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA155_35 = input.LA(1);

                         
                        int index155_35 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_35);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA155_36 = input.LA(1);

                         
                        int index155_36 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_36);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA155_37 = input.LA(1);

                         
                        int index155_37 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_37);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA155_38 = input.LA(1);

                         
                        int index155_38 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_38);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA155_39 = input.LA(1);

                         
                        int index155_39 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_39);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA155_40 = input.LA(1);

                         
                        int index155_40 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_40);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA155_41 = input.LA(1);

                         
                        int index155_41 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_41);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA155_42 = input.LA(1);

                         
                        int index155_42 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_42);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA155_43 = input.LA(1);

                         
                        int index155_43 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_43);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA155_44 = input.LA(1);

                         
                        int index155_44 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_44);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA155_45 = input.LA(1);

                         
                        int index155_45 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_45);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA155_46 = input.LA(1);

                         
                        int index155_46 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_46);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA155_47 = input.LA(1);

                         
                        int index155_47 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_47);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA155_48 = input.LA(1);

                         
                        int index155_48 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_48);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA155_49 = input.LA(1);

                         
                        int index155_49 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_49);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA155_50 = input.LA(1);

                         
                        int index155_50 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_50);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA155_51 = input.LA(1);

                         
                        int index155_51 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_51);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA155_52 = input.LA(1);

                         
                        int index155_52 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_52);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA155_53 = input.LA(1);

                         
                        int index155_53 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_53);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA155_54 = input.LA(1);

                         
                        int index155_54 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_54);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA155_55 = input.LA(1);

                         
                        int index155_55 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_55);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA155_56 = input.LA(1);

                         
                        int index155_56 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_56);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA155_58 = input.LA(1);

                         
                        int index155_58 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_58);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA155_59 = input.LA(1);

                         
                        int index155_59 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_59);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA155_60 = input.LA(1);

                         
                        int index155_60 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred238_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index155_60);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 155, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA156_eotS =
        "\50\uffff";
    static final String DFA156_eofS =
        "\1\1\47\uffff";
    static final String DFA156_minS =
        "\1\31\2\uffff\1\4\41\uffff\1\0\2\uffff";
    static final String DFA156_maxS =
        "\1\163\2\uffff\1\167\41\uffff\1\0\2\uffff";
    static final String DFA156_acceptS =
        "\1\uffff\1\2\45\uffff\1\1";
    static final String DFA156_specialS =
        "\45\uffff\1\0\2\uffff}>";
    static final String[] DFA156_transitionS = {
            "\1\1\2\uffff\1\3\1\1\3\uffff\4\1\1\uffff\1\1\2\uffff\2\1\1"+
            "\uffff\1\1\22\uffff\1\1\1\uffff\2\1\7\uffff\1\1\24\uffff\25"+
            "\1",
            "",
            "",
            "\1\45\31\uffff\1\1\2\uffff\1\1\36\uffff\1\1\65\uffff\2\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA156_eot = DFA.unpackEncodedString(DFA156_eotS);
    static final short[] DFA156_eof = DFA.unpackEncodedString(DFA156_eofS);
    static final char[] DFA156_min = DFA.unpackEncodedStringToUnsignedChars(DFA156_minS);
    static final char[] DFA156_max = DFA.unpackEncodedStringToUnsignedChars(DFA156_maxS);
    static final short[] DFA156_accept = DFA.unpackEncodedString(DFA156_acceptS);
    static final short[] DFA156_special = DFA.unpackEncodedString(DFA156_specialS);
    static final short[][] DFA156_transition;

    static {
        int numStates = DFA156_transitionS.length;
        DFA156_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA156_transition[i] = DFA.unpackEncodedString(DFA156_transitionS[i]);
        }
    }

    class DFA156 extends DFA {

        public DFA156(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 156;
            this.eot = DFA156_eot;
            this.eof = DFA156_eof;
            this.min = DFA156_min;
            this.max = DFA156_max;
            this.accept = DFA156_accept;
            this.special = DFA156_special;
            this.transition = DFA156_transition;
        }
        public String getDescription() {
            return "()* loopback of 947:21: ( '.' Identifier )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA156_37 = input.LA(1);

                         
                        int index156_37 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred241_Java()) ) {s = 39;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index156_37);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 156, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA157_eotS =
        "\77\uffff";
    static final String DFA157_eofS =
        "\1\4\76\uffff";
    static final String DFA157_minS =
        "\1\31\1\4\1\uffff\1\4\36\uffff\27\0\1\uffff\3\0\2\uffff";
    static final String DFA157_maxS =
        "\1\163\1\167\1\uffff\1\167\36\uffff\27\0\1\uffff\3\0\2\uffff";
    static final String DFA157_acceptS =
        "\2\uffff\1\1\1\uffff\1\2\72\uffff";
    static final String DFA157_specialS =
        "\42\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\uffff\1"+
        "\27\1\30\1\31\2\uffff}>";
    static final String[] DFA157_transitionS = {
            "\1\4\2\uffff\1\3\1\4\3\uffff\4\4\1\uffff\1\4\2\uffff\1\1\1"+
            "\4\1\uffff\1\4\22\uffff\1\4\1\uffff\1\2\1\4\7\uffff\1\4\24\uffff"+
            "\25\4",
            "\1\66\1\uffff\1\60\1\61\1\62\3\57\25\uffff\1\51\6\uffff\1"+
            "\70\1\uffff\1\2\14\uffff\10\67\1\uffff\1\53\1\50\2\uffff\1\64"+
            "\2\63\23\uffff\1\54\1\55\1\56\21\uffff\1\42\1\43\2\uffff\1\44"+
            "\1\45\1\46\1\47\1\52\1\65",
            "",
            "\1\4\31\uffff\1\2\2\uffff\1\2\36\uffff\1\73\65\uffff\1\72"+
            "\1\74",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA157_eot = DFA.unpackEncodedString(DFA157_eotS);
    static final short[] DFA157_eof = DFA.unpackEncodedString(DFA157_eofS);
    static final char[] DFA157_min = DFA.unpackEncodedStringToUnsignedChars(DFA157_minS);
    static final char[] DFA157_max = DFA.unpackEncodedStringToUnsignedChars(DFA157_maxS);
    static final short[] DFA157_accept = DFA.unpackEncodedString(DFA157_acceptS);
    static final short[] DFA157_special = DFA.unpackEncodedString(DFA157_specialS);
    static final short[][] DFA157_transition;

    static {
        int numStates = DFA157_transitionS.length;
        DFA157_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA157_transition[i] = DFA.unpackEncodedString(DFA157_transitionS[i]);
        }
    }

    class DFA157 extends DFA {

        public DFA157(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 157;
            this.eot = DFA157_eot;
            this.eof = DFA157_eof;
            this.min = DFA157_min;
            this.max = DFA157_max;
            this.accept = DFA157_accept;
            this.special = DFA157_special;
            this.transition = DFA157_transition;
        }
        public String getDescription() {
            return "947:39: ( identifierSuffix )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA157_34 = input.LA(1);

                         
                        int index157_34 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_34);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA157_35 = input.LA(1);

                         
                        int index157_35 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_35);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA157_36 = input.LA(1);

                         
                        int index157_36 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_36);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA157_37 = input.LA(1);

                         
                        int index157_37 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_37);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA157_38 = input.LA(1);

                         
                        int index157_38 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_38);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA157_39 = input.LA(1);

                         
                        int index157_39 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_39);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA157_40 = input.LA(1);

                         
                        int index157_40 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_40);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA157_41 = input.LA(1);

                         
                        int index157_41 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_41);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA157_42 = input.LA(1);

                         
                        int index157_42 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_42);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA157_43 = input.LA(1);

                         
                        int index157_43 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_43);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA157_44 = input.LA(1);

                         
                        int index157_44 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_44);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA157_45 = input.LA(1);

                         
                        int index157_45 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_45);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA157_46 = input.LA(1);

                         
                        int index157_46 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_46);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA157_47 = input.LA(1);

                         
                        int index157_47 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_47);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA157_48 = input.LA(1);

                         
                        int index157_48 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_48);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA157_49 = input.LA(1);

                         
                        int index157_49 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_49);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA157_50 = input.LA(1);

                         
                        int index157_50 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_50);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA157_51 = input.LA(1);

                         
                        int index157_51 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_51);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA157_52 = input.LA(1);

                         
                        int index157_52 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_52);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA157_53 = input.LA(1);

                         
                        int index157_53 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_53);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA157_54 = input.LA(1);

                         
                        int index157_54 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_54);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA157_55 = input.LA(1);

                         
                        int index157_55 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_55);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA157_56 = input.LA(1);

                         
                        int index157_56 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_56);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA157_58 = input.LA(1);

                         
                        int index157_58 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_58);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA157_59 = input.LA(1);

                         
                        int index157_59 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_59);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA157_60 = input.LA(1);

                         
                        int index157_60 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred242_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index157_60);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 157, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA158_eotS =
        "\50\uffff";
    static final String DFA158_eofS =
        "\1\1\47\uffff";
    static final String DFA158_minS =
        "\1\31\2\uffff\1\4\41\uffff\1\0\2\uffff";
    static final String DFA158_maxS =
        "\1\163\2\uffff\1\167\41\uffff\1\0\2\uffff";
    static final String DFA158_acceptS =
        "\1\uffff\1\2\45\uffff\1\1";
    static final String DFA158_specialS =
        "\45\uffff\1\0\2\uffff}>";
    static final String[] DFA158_transitionS = {
            "\1\1\2\uffff\1\3\1\1\3\uffff\4\1\1\uffff\1\1\2\uffff\2\1\1"+
            "\uffff\1\1\22\uffff\1\1\1\uffff\2\1\7\uffff\1\1\24\uffff\25"+
            "\1",
            "",
            "",
            "\1\45\31\uffff\1\1\2\uffff\1\1\36\uffff\1\1\65\uffff\2\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA158_eot = DFA.unpackEncodedString(DFA158_eotS);
    static final short[] DFA158_eof = DFA.unpackEncodedString(DFA158_eofS);
    static final char[] DFA158_min = DFA.unpackEncodedStringToUnsignedChars(DFA158_minS);
    static final char[] DFA158_max = DFA.unpackEncodedStringToUnsignedChars(DFA158_maxS);
    static final short[] DFA158_accept = DFA.unpackEncodedString(DFA158_acceptS);
    static final short[] DFA158_special = DFA.unpackEncodedString(DFA158_specialS);
    static final short[][] DFA158_transition;

    static {
        int numStates = DFA158_transitionS.length;
        DFA158_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA158_transition[i] = DFA.unpackEncodedString(DFA158_transitionS[i]);
        }
    }

    class DFA158 extends DFA {

        public DFA158(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 158;
            this.eot = DFA158_eot;
            this.eof = DFA158_eof;
            this.min = DFA158_min;
            this.max = DFA158_max;
            this.accept = DFA158_accept;
            this.special = DFA158_special;
            this.transition = DFA158_transition;
        }
        public String getDescription() {
            return "()* loopback of 950:126: ( '.' Identifier )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA158_37 = input.LA(1);

                         
                        int index158_37 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred246_Java()) ) {s = 39;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index158_37);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 158, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA159_eotS =
        "\77\uffff";
    static final String DFA159_eofS =
        "\1\4\76\uffff";
    static final String DFA159_minS =
        "\1\31\1\4\1\uffff\1\4\36\uffff\27\0\1\uffff\3\0\2\uffff";
    static final String DFA159_maxS =
        "\1\163\1\167\1\uffff\1\167\36\uffff\27\0\1\uffff\3\0\2\uffff";
    static final String DFA159_acceptS =
        "\2\uffff\1\1\1\uffff\1\2\72\uffff";
    static final String DFA159_specialS =
        "\42\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\uffff\1"+
        "\27\1\30\1\31\2\uffff}>";
    static final String[] DFA159_transitionS = {
            "\1\4\2\uffff\1\3\1\4\3\uffff\4\4\1\uffff\1\4\2\uffff\1\1\1"+
            "\4\1\uffff\1\4\22\uffff\1\4\1\uffff\1\2\1\4\7\uffff\1\4\24\uffff"+
            "\25\4",
            "\1\66\1\uffff\1\60\1\61\1\62\3\57\25\uffff\1\51\6\uffff\1"+
            "\70\1\uffff\1\2\14\uffff\10\67\1\uffff\1\53\1\50\2\uffff\1\64"+
            "\2\63\23\uffff\1\54\1\55\1\56\21\uffff\1\42\1\43\2\uffff\1\44"+
            "\1\45\1\46\1\47\1\52\1\65",
            "",
            "\1\4\31\uffff\1\2\2\uffff\1\2\36\uffff\1\73\65\uffff\1\72"+
            "\1\74",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA159_eot = DFA.unpackEncodedString(DFA159_eotS);
    static final short[] DFA159_eof = DFA.unpackEncodedString(DFA159_eofS);
    static final char[] DFA159_min = DFA.unpackEncodedStringToUnsignedChars(DFA159_minS);
    static final char[] DFA159_max = DFA.unpackEncodedStringToUnsignedChars(DFA159_maxS);
    static final short[] DFA159_accept = DFA.unpackEncodedString(DFA159_acceptS);
    static final short[] DFA159_special = DFA.unpackEncodedString(DFA159_specialS);
    static final short[][] DFA159_transition;

    static {
        int numStates = DFA159_transitionS.length;
        DFA159_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA159_transition[i] = DFA.unpackEncodedString(DFA159_transitionS[i]);
        }
    }

    class DFA159 extends DFA {

        public DFA159(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 159;
            this.eot = DFA159_eot;
            this.eof = DFA159_eof;
            this.min = DFA159_min;
            this.max = DFA159_max;
            this.accept = DFA159_accept;
            this.special = DFA159_special;
            this.transition = DFA159_transition;
        }
        public String getDescription() {
            return "950:144: ( identifierSuffix )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA159_34 = input.LA(1);

                         
                        int index159_34 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_34);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA159_35 = input.LA(1);

                         
                        int index159_35 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_35);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA159_36 = input.LA(1);

                         
                        int index159_36 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_36);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA159_37 = input.LA(1);

                         
                        int index159_37 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_37);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA159_38 = input.LA(1);

                         
                        int index159_38 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_38);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA159_39 = input.LA(1);

                         
                        int index159_39 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_39);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA159_40 = input.LA(1);

                         
                        int index159_40 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_40);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA159_41 = input.LA(1);

                         
                        int index159_41 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_41);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA159_42 = input.LA(1);

                         
                        int index159_42 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_42);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA159_43 = input.LA(1);

                         
                        int index159_43 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_43);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA159_44 = input.LA(1);

                         
                        int index159_44 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_44);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA159_45 = input.LA(1);

                         
                        int index159_45 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_45);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA159_46 = input.LA(1);

                         
                        int index159_46 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_46);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA159_47 = input.LA(1);

                         
                        int index159_47 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_47);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA159_48 = input.LA(1);

                         
                        int index159_48 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_48);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA159_49 = input.LA(1);

                         
                        int index159_49 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_49);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA159_50 = input.LA(1);

                         
                        int index159_50 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_50);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA159_51 = input.LA(1);

                         
                        int index159_51 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_51);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA159_52 = input.LA(1);

                         
                        int index159_52 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_52);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA159_53 = input.LA(1);

                         
                        int index159_53 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_53);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA159_54 = input.LA(1);

                         
                        int index159_54 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_54);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA159_55 = input.LA(1);

                         
                        int index159_55 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_55);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA159_56 = input.LA(1);

                         
                        int index159_56 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_56);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA159_58 = input.LA(1);

                         
                        int index159_58 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_58);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA159_59 = input.LA(1);

                         
                        int index159_59 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_59);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA159_60 = input.LA(1);

                         
                        int index159_60 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred247_Java()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index159_60);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 159, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA165_eotS =
        "\41\uffff";
    static final String DFA165_eofS =
        "\41\uffff";
    static final String DFA165_minS =
        "\1\34\1\4\1\uffff\1\36\35\uffff";
    static final String DFA165_maxS =
        "\1\101\1\167\1\uffff\1\167\35\uffff";
    static final String DFA165_acceptS =
        "\2\uffff\1\3\1\uffff\1\1\1\2\26\uffff\1\4\1\6\1\7\1\10\1\5";
    static final String DFA165_specialS =
        "\41\uffff}>";
    static final String[] DFA165_transitionS = {
            "\1\3\14\uffff\1\1\27\uffff\1\2",
            "\1\5\1\uffff\6\5\25\uffff\1\5\6\uffff\1\5\1\uffff\1\4\14\uffff"+
            "\10\5\1\uffff\2\5\2\uffff\3\5\23\uffff\3\5\21\uffff\2\5\2\uffff"+
            "\6\5",
            "",
            "\1\34\2\uffff\1\40\36\uffff\1\36\65\uffff\1\35\1\37",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA165_eot = DFA.unpackEncodedString(DFA165_eotS);
    static final short[] DFA165_eof = DFA.unpackEncodedString(DFA165_eofS);
    static final char[] DFA165_min = DFA.unpackEncodedStringToUnsignedChars(DFA165_minS);
    static final char[] DFA165_max = DFA.unpackEncodedStringToUnsignedChars(DFA165_maxS);
    static final short[] DFA165_accept = DFA.unpackEncodedString(DFA165_acceptS);
    static final short[] DFA165_special = DFA.unpackEncodedString(DFA165_specialS);
    static final short[][] DFA165_transition;

    static {
        int numStates = DFA165_transitionS.length;
        DFA165_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA165_transition[i] = DFA.unpackEncodedString(DFA165_transitionS[i]);
        }
    }

    class DFA165 extends DFA {

        public DFA165(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 165;
            this.eot = DFA165_eot;
            this.eof = DFA165_eof;
            this.min = DFA165_min;
            this.max = DFA165_max;
            this.accept = DFA165_accept;
            this.special = DFA165_special;
            this.transition = DFA165_transition;
        }
        public String getDescription() {
            return "955:1: identifierSuffix : ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' explicitGenericInvocation | '.' 'this' | '.' 'super' arguments | '.' 'new' ( nonWildcardTypeArguments )? innerCreator );";
        }
    }
    static final String DFA163_eotS =
        "\70\uffff";
    static final String DFA163_eofS =
        "\1\1\67\uffff";
    static final String DFA163_minS =
        "\1\31\35\uffff\1\4\1\uffff\27\0\1\uffff";
    static final String DFA163_maxS =
        "\1\163\35\uffff\1\167\1\uffff\27\0\1\uffff";
    static final String DFA163_acceptS =
        "\1\uffff\1\2\65\uffff\1\1";
    static final String DFA163_specialS =
        "\40\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\uffff}>";
    static final String[] DFA163_transitionS = {
            "\1\1\2\uffff\2\1\3\uffff\4\1\1\uffff\1\1\2\uffff\1\36\1\1\1"+
            "\uffff\1\1\22\uffff\1\1\2\uffff\1\1\7\uffff\1\1\24\uffff\25"+
            "\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\64\1\uffff\1\56\1\57\1\60\3\55\25\uffff\1\47\6\uffff\1"+
            "\66\16\uffff\10\65\1\uffff\1\51\1\46\2\uffff\1\62\2\61\23\uffff"+
            "\1\52\1\53\1\54\21\uffff\1\40\1\41\2\uffff\1\42\1\43\1\44\1"+
            "\45\1\50\1\63",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            ""
    };

    static final short[] DFA163_eot = DFA.unpackEncodedString(DFA163_eotS);
    static final short[] DFA163_eof = DFA.unpackEncodedString(DFA163_eofS);
    static final char[] DFA163_min = DFA.unpackEncodedStringToUnsignedChars(DFA163_minS);
    static final char[] DFA163_max = DFA.unpackEncodedStringToUnsignedChars(DFA163_maxS);
    static final short[] DFA163_accept = DFA.unpackEncodedString(DFA163_acceptS);
    static final short[] DFA163_special = DFA.unpackEncodedString(DFA163_specialS);
    static final short[][] DFA163_transition;

    static {
        int numStates = DFA163_transitionS.length;
        DFA163_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA163_transition[i] = DFA.unpackEncodedString(DFA163_transitionS[i]);
        }
    }

    class DFA163 extends DFA {

        public DFA163(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 163;
            this.eot = DFA163_eot;
            this.eof = DFA163_eof;
            this.min = DFA163_min;
            this.max = DFA163_max;
            this.accept = DFA163_accept;
            this.special = DFA163_special;
            this.transition = DFA163_transition;
        }
        public String getDescription() {
            return "()+ loopback of 957:4: ( '[' expression ']' )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA163_32 = input.LA(1);

                         
                        int index163_32 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_32);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA163_33 = input.LA(1);

                         
                        int index163_33 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_33);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA163_34 = input.LA(1);

                         
                        int index163_34 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_34);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA163_35 = input.LA(1);

                         
                        int index163_35 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_35);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA163_36 = input.LA(1);

                         
                        int index163_36 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_36);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA163_37 = input.LA(1);

                         
                        int index163_37 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_37);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA163_38 = input.LA(1);

                         
                        int index163_38 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_38);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA163_39 = input.LA(1);

                         
                        int index163_39 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_39);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA163_40 = input.LA(1);

                         
                        int index163_40 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_40);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA163_41 = input.LA(1);

                         
                        int index163_41 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_41);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA163_42 = input.LA(1);

                         
                        int index163_42 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_42);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA163_43 = input.LA(1);

                         
                        int index163_43 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_43);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA163_44 = input.LA(1);

                         
                        int index163_44 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_44);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA163_45 = input.LA(1);

                         
                        int index163_45 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_45);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA163_46 = input.LA(1);

                         
                        int index163_46 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_46);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA163_47 = input.LA(1);

                         
                        int index163_47 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_47);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA163_48 = input.LA(1);

                         
                        int index163_48 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_48);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA163_49 = input.LA(1);

                         
                        int index163_49 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_49);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA163_50 = input.LA(1);

                         
                        int index163_50 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_50);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA163_51 = input.LA(1);

                         
                        int index163_51 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_51);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA163_52 = input.LA(1);

                         
                        int index163_52 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_52);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA163_53 = input.LA(1);

                         
                        int index163_53 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_53);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA163_54 = input.LA(1);

                         
                        int index163_54 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred253_Java()) ) {s = 55;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index163_54);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 163, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA175_eotS =
        "\31\uffff";
    static final String DFA175_eofS =
        "\31\uffff";
    static final String DFA175_minS =
        "\1\4\30\uffff";
    static final String DFA175_maxS =
        "\1\167\30\uffff";
    static final String DFA175_acceptS =
        "\1\uffff\1\1\1\2\26\uffff";
    static final String DFA175_specialS =
        "\31\uffff}>";
    static final String[] DFA175_transitionS = {
            "\1\2\1\uffff\6\2\25\uffff\1\2\6\uffff\1\2\1\uffff\1\1\14\uffff"+
            "\10\2\1\uffff\2\2\2\uffff\3\2\23\uffff\3\2\21\uffff\2\2\2\uffff"+
            "\6\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA175_eot = DFA.unpackEncodedString(DFA175_eotS);
    static final short[] DFA175_eof = DFA.unpackEncodedString(DFA175_eofS);
    static final char[] DFA175_min = DFA.unpackEncodedStringToUnsignedChars(DFA175_minS);
    static final char[] DFA175_max = DFA.unpackEncodedStringToUnsignedChars(DFA175_maxS);
    static final short[] DFA175_accept = DFA.unpackEncodedString(DFA175_acceptS);
    static final short[] DFA175_special = DFA.unpackEncodedString(DFA175_specialS);
    static final short[][] DFA175_transition;

    static {
        int numStates = DFA175_transitionS.length;
        DFA175_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA175_transition[i] = DFA.unpackEncodedString(DFA175_transitionS[i]);
        }
    }

    class DFA175 extends DFA {

        public DFA175(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 175;
            this.eot = DFA175_eot;
            this.eof = DFA175_eof;
            this.min = DFA175_min;
            this.max = DFA175_max;
            this.accept = DFA175_accept;
            this.special = DFA175_special;
            this.transition = DFA175_transition;
        }
        public String getDescription() {
            return "983:9: ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* )";
        }
    }
    static final String DFA173_eotS =
        "\71\uffff";
    static final String DFA173_eofS =
        "\1\2\70\uffff";
    static final String DFA173_minS =
        "\1\31\1\4\37\uffff\27\0\1\uffff";
    static final String DFA173_maxS =
        "\1\163\1\167\37\uffff\27\0\1\uffff";
    static final String DFA173_acceptS =
        "\2\uffff\1\2\65\uffff\1\1";
    static final String DFA173_specialS =
        "\41\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\uffff}>";
    static final String[] DFA173_transitionS = {
            "\1\2\2\uffff\2\2\3\uffff\4\2\1\uffff\1\2\2\uffff\1\1\1\2\1"+
            "\uffff\1\2\22\uffff\1\2\2\uffff\1\2\7\uffff\1\2\24\uffff\25"+
            "\2",
            "\1\65\1\uffff\1\57\1\60\1\61\3\56\25\uffff\1\50\6\uffff\1"+
            "\67\1\uffff\1\2\14\uffff\10\66\1\uffff\1\52\1\47\2\uffff\1\63"+
            "\2\62\23\uffff\1\53\1\54\1\55\21\uffff\1\41\1\42\2\uffff\1\43"+
            "\1\44\1\45\1\46\1\51\1\64",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            ""
    };

    static final short[] DFA173_eot = DFA.unpackEncodedString(DFA173_eotS);
    static final short[] DFA173_eof = DFA.unpackEncodedString(DFA173_eofS);
    static final char[] DFA173_min = DFA.unpackEncodedStringToUnsignedChars(DFA173_minS);
    static final char[] DFA173_max = DFA.unpackEncodedStringToUnsignedChars(DFA173_maxS);
    static final short[] DFA173_accept = DFA.unpackEncodedString(DFA173_acceptS);
    static final short[] DFA173_special = DFA.unpackEncodedString(DFA173_specialS);
    static final short[][] DFA173_transition;

    static {
        int numStates = DFA173_transitionS.length;
        DFA173_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA173_transition[i] = DFA.unpackEncodedString(DFA173_transitionS[i]);
        }
    }

    class DFA173 extends DFA {

        public DFA173(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 173;
            this.eot = DFA173_eot;
            this.eof = DFA173_eof;
            this.min = DFA173_min;
            this.max = DFA173_max;
            this.accept = DFA173_accept;
            this.special = DFA173_special;
            this.transition = DFA173_transition;
        }
        public String getDescription() {
            return "()* loopback of 984:28: ( '[' expression ']' )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA173_33 = input.LA(1);

                         
                        int index173_33 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_33);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA173_34 = input.LA(1);

                         
                        int index173_34 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_34);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA173_35 = input.LA(1);

                         
                        int index173_35 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_35);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA173_36 = input.LA(1);

                         
                        int index173_36 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_36);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA173_37 = input.LA(1);

                         
                        int index173_37 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_37);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA173_38 = input.LA(1);

                         
                        int index173_38 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_38);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA173_39 = input.LA(1);

                         
                        int index173_39 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_39);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA173_40 = input.LA(1);

                         
                        int index173_40 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_40);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA173_41 = input.LA(1);

                         
                        int index173_41 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_41);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA173_42 = input.LA(1);

                         
                        int index173_42 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_42);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA173_43 = input.LA(1);

                         
                        int index173_43 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_43);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA173_44 = input.LA(1);

                         
                        int index173_44 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_44);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA173_45 = input.LA(1);

                         
                        int index173_45 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_45);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA173_46 = input.LA(1);

                         
                        int index173_46 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_46);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA173_47 = input.LA(1);

                         
                        int index173_47 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_47);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA173_48 = input.LA(1);

                         
                        int index173_48 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_48);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA173_49 = input.LA(1);

                         
                        int index173_49 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_49);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA173_50 = input.LA(1);

                         
                        int index173_50 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_50);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA173_51 = input.LA(1);

                         
                        int index173_51 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_51);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA173_52 = input.LA(1);

                         
                        int index173_52 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_52);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA173_53 = input.LA(1);

                         
                        int index173_53 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_53);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA173_54 = input.LA(1);

                         
                        int index173_54 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_54);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA173_55 = input.LA(1);

                         
                        int index173_55 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred269_Java()) ) {s = 56;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index173_55);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 173, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA174_eotS =
        "\70\uffff";
    static final String DFA174_eofS =
        "\1\1\67\uffff";
    static final String DFA174_minS =
        "\1\31\35\uffff\1\4\31\uffff";
    static final String DFA174_maxS =
        "\1\163\35\uffff\1\167\31\uffff";
    static final String DFA174_acceptS =
        "\1\uffff\1\2\36\uffff\1\1\27\uffff";
    static final String DFA174_specialS =
        "\70\uffff}>";
    static final String[] DFA174_transitionS = {
            "\1\1\2\uffff\2\1\3\uffff\4\1\1\uffff\1\1\2\uffff\1\36\1\1\1"+
            "\uffff\1\1\22\uffff\1\1\2\uffff\1\1\7\uffff\1\1\24\uffff\25"+
            "\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\1\1\uffff\6\1\25\uffff\1\1\6\uffff\1\1\1\uffff\1\40\14"+
            "\uffff\10\1\1\uffff\2\1\2\uffff\3\1\23\uffff\3\1\21\uffff\2"+
            "\1\2\uffff\6\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA174_eot = DFA.unpackEncodedString(DFA174_eotS);
    static final short[] DFA174_eof = DFA.unpackEncodedString(DFA174_eofS);
    static final char[] DFA174_min = DFA.unpackEncodedStringToUnsignedChars(DFA174_minS);
    static final char[] DFA174_max = DFA.unpackEncodedStringToUnsignedChars(DFA174_maxS);
    static final short[] DFA174_accept = DFA.unpackEncodedString(DFA174_acceptS);
    static final short[] DFA174_special = DFA.unpackEncodedString(DFA174_specialS);
    static final short[][] DFA174_transition;

    static {
        int numStates = DFA174_transitionS.length;
        DFA174_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA174_transition[i] = DFA.unpackEncodedString(DFA174_transitionS[i]);
        }
    }

    class DFA174 extends DFA {

        public DFA174(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 174;
            this.eot = DFA174_eot;
            this.eof = DFA174_eof;
            this.min = DFA174_min;
            this.max = DFA174_max;
            this.accept = DFA174_accept;
            this.special = DFA174_special;
            this.transition = DFA174_transition;
        }
        public String getDescription() {
            return "()* loopback of 984:50: ( '[' ']' )*";
        }
    }
    static final String DFA176_eotS =
        "\41\uffff";
    static final String DFA176_eofS =
        "\1\2\40\uffff";
    static final String DFA176_minS =
        "\1\31\40\uffff";
    static final String DFA176_maxS =
        "\1\163\40\uffff";
    static final String DFA176_acceptS =
        "\1\uffff\1\1\1\2\36\uffff";
    static final String DFA176_specialS =
        "\41\uffff}>";
    static final String[] DFA176_transitionS = {
            "\1\2\2\uffff\2\2\3\uffff\4\2\1\1\1\2\2\uffff\2\2\1\uffff\1"+
            "\2\22\uffff\1\2\2\uffff\1\2\7\uffff\1\2\24\uffff\25\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA176_eot = DFA.unpackEncodedString(DFA176_eotS);
    static final short[] DFA176_eof = DFA.unpackEncodedString(DFA176_eofS);
    static final char[] DFA176_min = DFA.unpackEncodedStringToUnsignedChars(DFA176_minS);
    static final char[] DFA176_max = DFA.unpackEncodedStringToUnsignedChars(DFA176_maxS);
    static final short[] DFA176_accept = DFA.unpackEncodedString(DFA176_acceptS);
    static final short[] DFA176_special = DFA.unpackEncodedString(DFA176_specialS);
    static final short[][] DFA176_transition;

    static {
        int numStates = DFA176_transitionS.length;
        DFA176_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA176_transition[i] = DFA.unpackEncodedString(DFA176_transitionS[i]);
        }
    }

    class DFA176 extends DFA {

        public DFA176(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 176;
            this.eot = DFA176_eot;
            this.eof = DFA176_eof;
            this.min = DFA176_min;
            this.max = DFA176_max;
            this.accept = DFA176_accept;
            this.special = DFA176_special;
            this.transition = DFA176_transition;
        }
        public String getDescription() {
            return "989:14: ( classBody )?";
        }
    }
    static final String DFA178_eotS =
        "\41\uffff";
    static final String DFA178_eofS =
        "\1\2\40\uffff";
    static final String DFA178_minS =
        "\1\31\40\uffff";
    static final String DFA178_maxS =
        "\1\163\40\uffff";
    static final String DFA178_acceptS =
        "\1\uffff\1\1\1\2\36\uffff";
    static final String DFA178_specialS =
        "\41\uffff}>";
    static final String[] DFA178_transitionS = {
            "\1\2\2\uffff\2\2\3\uffff\4\2\1\uffff\1\2\2\uffff\2\2\1\uffff"+
            "\1\2\22\uffff\1\2\1\uffff\1\1\1\2\7\uffff\1\2\24\uffff\25\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA178_eot = DFA.unpackEncodedString(DFA178_eotS);
    static final short[] DFA178_eof = DFA.unpackEncodedString(DFA178_eofS);
    static final char[] DFA178_min = DFA.unpackEncodedStringToUnsignedChars(DFA178_minS);
    static final char[] DFA178_max = DFA.unpackEncodedStringToUnsignedChars(DFA178_maxS);
    static final short[] DFA178_accept = DFA.unpackEncodedString(DFA178_acceptS);
    static final short[] DFA178_special = DFA.unpackEncodedString(DFA178_specialS);
    static final short[][] DFA178_transition;

    static {
        int numStates = DFA178_transitionS.length;
        DFA178_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA178_transition[i] = DFA.unpackEncodedString(DFA178_transitionS[i]);
        }
    }

    class DFA178 extends DFA {

        public DFA178(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 178;
            this.eot = DFA178_eot;
            this.eof = DFA178_eof;
            this.min = DFA178_min;
            this.max = DFA178_max;
            this.accept = DFA178_accept;
            this.special = DFA178_special;
            this.transition = DFA178_transition;
        }
        public String getDescription() {
            return "1006:19: ( arguments )?";
        }
    }
    static final String DFA181_eotS =
        "\41\uffff";
    static final String DFA181_eofS =
        "\1\2\40\uffff";
    static final String DFA181_minS =
        "\1\31\40\uffff";
    static final String DFA181_maxS =
        "\1\163\40\uffff";
    static final String DFA181_acceptS =
        "\1\uffff\1\1\1\2\36\uffff";
    static final String DFA181_specialS =
        "\41\uffff}>";
    static final String[] DFA181_transitionS = {
            "\1\2\2\uffff\2\2\3\uffff\4\2\1\uffff\1\2\2\uffff\2\2\1\uffff"+
            "\1\2\22\uffff\1\2\1\uffff\1\1\1\2\7\uffff\1\2\24\uffff\25\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA181_eot = DFA.unpackEncodedString(DFA181_eotS);
    static final short[] DFA181_eof = DFA.unpackEncodedString(DFA181_eofS);
    static final char[] DFA181_min = DFA.unpackEncodedStringToUnsignedChars(DFA181_minS);
    static final char[] DFA181_max = DFA.unpackEncodedStringToUnsignedChars(DFA181_maxS);
    static final short[] DFA181_accept = DFA.unpackEncodedString(DFA181_acceptS);
    static final short[] DFA181_special = DFA.unpackEncodedString(DFA181_specialS);
    static final short[][] DFA181_transition;

    static {
        int numStates = DFA181_transitionS.length;
        DFA181_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA181_transition[i] = DFA.unpackEncodedString(DFA181_transitionS[i]);
        }
    }

    class DFA181 extends DFA {

        public DFA181(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 181;
            this.eot = DFA181_eot;
            this.eof = DFA181_eof;
            this.min = DFA181_min;
            this.max = DFA181_max;
            this.accept = DFA181_accept;
            this.special = DFA181_special;
            this.transition = DFA181_transition;
        }
        public String getDescription() {
            return "1015:21: ( arguments )?";
        }
    }
    static final String DFA183_eotS =
        "\31\uffff";
    static final String DFA183_eofS =
        "\31\uffff";
    static final String DFA183_minS =
        "\1\4\30\uffff";
    static final String DFA183_maxS =
        "\1\167\30\uffff";
    static final String DFA183_acceptS =
        "\1\uffff\1\1\26\uffff\1\2";
    static final String DFA183_specialS =
        "\31\uffff}>";
    static final String[] DFA183_transitionS = {
            "\1\1\1\uffff\6\1\25\uffff\1\1\6\uffff\1\1\16\uffff\10\1\1\uffff"+
            "\2\1\1\30\1\uffff\3\1\23\uffff\3\1\21\uffff\2\1\2\uffff\6\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA183_eot = DFA.unpackEncodedString(DFA183_eotS);
    static final short[] DFA183_eof = DFA.unpackEncodedString(DFA183_eofS);
    static final char[] DFA183_min = DFA.unpackEncodedStringToUnsignedChars(DFA183_minS);
    static final char[] DFA183_max = DFA.unpackEncodedStringToUnsignedChars(DFA183_maxS);
    static final short[] DFA183_accept = DFA.unpackEncodedString(DFA183_acceptS);
    static final short[] DFA183_special = DFA.unpackEncodedString(DFA183_specialS);
    static final short[][] DFA183_transition;

    static {
        int numStates = DFA183_transitionS.length;
        DFA183_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA183_transition[i] = DFA.unpackEncodedString(DFA183_transitionS[i]);
        }
    }

    class DFA183 extends DFA {

        public DFA183(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 183;
            this.eot = DFA183_eot;
            this.eof = DFA183_eof;
            this.min = DFA183_min;
            this.max = DFA183_max;
            this.accept = DFA183_accept;
            this.special = DFA183_special;
            this.transition = DFA183_transition;
        }
        public String getDescription() {
            return "1019:8: ( expressionList )?";
        }
    }
 

    public static final BitSet FOLLOW_annotations_in_compilationUnit70 = new BitSet(new long[]{0x007FE0804F000022L,0x0000000000000080L});
    public static final BitSet FOLLOW_packageDeclaration_in_compilationUnit75 = new BitSet(new long[]{0x007FE0804E000022L,0x0000000000000080L});
    public static final BitSet FOLLOW_importDeclaration_in_compilationUnit86 = new BitSet(new long[]{0x007FE0804E000022L,0x0000000000000080L});
    public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit97 = new BitSet(new long[]{0x007FE0804A000022L,0x0000000000000080L});
    public static final BitSet FOLLOW_24_in_packageDeclaration109 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedName_in_packageDeclaration111 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_packageDeclaration113 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_importDeclaration125 = new BitSet(new long[]{0x0000000008000010L});
    public static final BitSet FOLLOW_27_in_importDeclaration127 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_importDeclaration130 = new BitSet(new long[]{0x0000000012000000L});
    public static final BitSet FOLLOW_28_in_importDeclaration133 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_importDeclaration135 = new BitSet(new long[]{0x0000000012000000L});
    public static final BitSet FOLLOW_28_in_importDeclaration140 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_29_in_importDeclaration142 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_importDeclaration146 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_typeDeclaration168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_classOrInterfaceDeclaration180 = new BitSet(new long[]{0x007FE08048000020L,0x0000000000000080L});
    public static final BitSet FOLLOW_classDeclaration_in_classOrInterfaceDeclaration184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_classDeclaration201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_classDeclaration211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_normalClassDeclaration223 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_normalClassDeclaration225 = new BitSet(new long[]{0x0000002380000000L});
    public static final BitSet FOLLOW_typeParameters_in_normalClassDeclaration228 = new BitSet(new long[]{0x0000002380000000L});
    public static final BitSet FOLLOW_31_in_normalClassDeclaration241 = new BitSet(new long[]{0x7F80000000000010L});
    public static final BitSet FOLLOW_type_in_normalClassDeclaration243 = new BitSet(new long[]{0x0000002380000000L});
    public static final BitSet FOLLOW_32_in_normalClassDeclaration256 = new BitSet(new long[]{0x7F80000000000010L});
    public static final BitSet FOLLOW_typeList_in_normalClassDeclaration258 = new BitSet(new long[]{0x0000002380000000L});
    public static final BitSet FOLLOW_classBody_in_normalClassDeclaration270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_typeParameters282 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters284 = new BitSet(new long[]{0x0000000C00000000L});
    public static final BitSet FOLLOW_34_in_typeParameters287 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters289 = new BitSet(new long[]{0x0000000C00000000L});
    public static final BitSet FOLLOW_35_in_typeParameters293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_typeParameter304 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_31_in_typeParameter307 = new BitSet(new long[]{0x7F80000000000010L});
    public static final BitSet FOLLOW_bound_in_typeParameter309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_bound324 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_36_in_bound327 = new BitSet(new long[]{0x7F80000000000010L});
    public static final BitSet FOLLOW_type_in_bound329 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_ENUM_in_enumDeclaration342 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_enumDeclaration344 = new BitSet(new long[]{0x0000002100000000L});
    public static final BitSet FOLLOW_32_in_enumDeclaration347 = new BitSet(new long[]{0x7F80000000000010L});
    public static final BitSet FOLLOW_typeList_in_enumDeclaration349 = new BitSet(new long[]{0x0000002100000000L});
    public static final BitSet FOLLOW_enumBody_in_enumDeclaration353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_enumBody365 = new BitSet(new long[]{0x0000004402000010L,0x0000000000000080L});
    public static final BitSet FOLLOW_enumConstants_in_enumBody367 = new BitSet(new long[]{0x0000004402000000L});
    public static final BitSet FOLLOW_34_in_enumBody370 = new BitSet(new long[]{0x0000004002000000L});
    public static final BitSet FOLLOW_enumBodyDeclarations_in_enumBody373 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_38_in_enumBody376 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants387 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_34_in_enumConstants390 = new BitSet(new long[]{0x0000000000000010L,0x0000000000000080L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants392 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_annotations_in_enumConstant406 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_enumConstant409 = new BitSet(new long[]{0x0000002380000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_enumConstant412 = new BitSet(new long[]{0x0000002380000002L});
    public static final BitSet FOLLOW_classBody_in_enumConstant417 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_enumBodyDeclarations431 = new BitSet(new long[]{0x7FFFE1A24A000032L,0x0000000000000080L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_enumBodyDeclarations434 = new BitSet(new long[]{0x7FFFE1A24A000032L,0x0000000000000080L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration448 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration454 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_normalInterfaceDeclaration466 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_normalInterfaceDeclaration468 = new BitSet(new long[]{0x0000002280000000L});
    public static final BitSet FOLLOW_typeParameters_in_normalInterfaceDeclaration470 = new BitSet(new long[]{0x0000002280000000L});
    public static final BitSet FOLLOW_31_in_normalInterfaceDeclaration474 = new BitSet(new long[]{0x7F80000000000010L});
    public static final BitSet FOLLOW_typeList_in_normalInterfaceDeclaration476 = new BitSet(new long[]{0x0000002280000000L});
    public static final BitSet FOLLOW_interfaceBody_in_normalInterfaceDeclaration480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeList492 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_34_in_typeList495 = new BitSet(new long[]{0x7F80000000000010L});
    public static final BitSet FOLLOW_type_in_typeList497 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_37_in_classBody511 = new BitSet(new long[]{0x7FFFE1E24A000030L,0x0000000000000080L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_classBody513 = new BitSet(new long[]{0x7FFFE1E24A000030L,0x0000000000000080L});
    public static final BitSet FOLLOW_38_in_classBody516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_interfaceBody528 = new BitSet(new long[]{0x7FFFE1C24A000030L,0x0000000000000080L});
    public static final BitSet FOLLOW_interfaceBodyDeclaration_in_interfaceBody530 = new BitSet(new long[]{0x7FFFE1C24A000030L,0x0000000000000080L});
    public static final BitSet FOLLOW_38_in_interfaceBody533 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_classBodyDeclaration544 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_classBodyDeclaration549 = new BitSet(new long[]{0x0000002008000000L});
    public static final BitSet FOLLOW_block_in_classBodyDeclaration552 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_classBodyDeclaration557 = new BitSet(new long[]{0x7FFFE1A24A000030L,0x0000000000000080L});
    public static final BitSet FOLLOW_memberDecl_in_classBodyDeclaration560 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_genericMethodOrConstructorDecl_in_memberDecl572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodDeclaration_in_memberDecl577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldDeclaration_in_memberDecl582 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_memberDecl587 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_memberDecl589 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_voidMethodDeclaratorRest_in_memberDecl591 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_memberDecl596 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_constructorDeclaratorRest_in_memberDecl598 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_memberDecl603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_memberDecl608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeParameters_in_genericMethodOrConstructorDecl620 = new BitSet(new long[]{0x7F80010000000010L});
    public static final BitSet FOLLOW_genericMethodOrConstructorRest_in_genericMethodOrConstructorDecl622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_genericMethodOrConstructorRest635 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_40_in_genericMethodOrConstructorRest639 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_genericMethodOrConstructorRest642 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_methodDeclaratorRest_in_genericMethodOrConstructorRest644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_genericMethodOrConstructorRest649 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_constructorDeclaratorRest_in_genericMethodOrConstructorRest651 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_methodDeclaration662 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_methodDeclaration664 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_methodDeclaratorRest_in_methodDeclaration666 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_fieldDeclaration677 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclarators_in_fieldDeclaration679 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_fieldDeclaration681 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_interfaceBodyDeclaration694 = new BitSet(new long[]{0x7FFFE18248000030L,0x0000000000000080L});
    public static final BitSet FOLLOW_interfaceMemberDecl_in_interfaceBodyDeclaration697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_interfaceBodyDeclaration704 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodOrFieldDecl_in_interfaceMemberDecl715 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceGenericMethodDecl_in_interfaceMemberDecl722 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_interfaceMemberDecl732 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_interfaceMemberDecl734 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_voidInterfaceMethodDeclaratorRest_in_interfaceMemberDecl736 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_interfaceMemberDecl746 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_interfaceMemberDecl756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_interfaceMethodOrFieldDecl768 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_interfaceMethodOrFieldDecl770 = new BitSet(new long[]{0x0000120000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodOrFieldRest_in_interfaceMethodOrFieldDecl772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constantDeclaratorsRest_in_interfaceMethodOrFieldRest784 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_interfaceMethodOrFieldRest786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodDeclaratorRest_in_interfaceMethodOrFieldRest791 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_methodDeclaratorRest803 = new BitSet(new long[]{0x00000A200A000000L});
    public static final BitSet FOLLOW_41_in_methodDeclaratorRest806 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_methodDeclaratorRest808 = new BitSet(new long[]{0x00000A200A000000L});
    public static final BitSet FOLLOW_43_in_methodDeclaratorRest821 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_methodDeclaratorRest823 = new BitSet(new long[]{0x000000200A000000L});
    public static final BitSet FOLLOW_methodBody_in_methodDeclaratorRest839 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_methodDeclaratorRest853 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_voidMethodDeclaratorRest875 = new BitSet(new long[]{0x000008200A000000L});
    public static final BitSet FOLLOW_43_in_voidMethodDeclaratorRest878 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_voidMethodDeclaratorRest880 = new BitSet(new long[]{0x000000200A000000L});
    public static final BitSet FOLLOW_methodBody_in_voidMethodDeclaratorRest896 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_voidMethodDeclaratorRest910 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_interfaceMethodDeclaratorRest932 = new BitSet(new long[]{0x00000A0002000000L});
    public static final BitSet FOLLOW_41_in_interfaceMethodDeclaratorRest935 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_interfaceMethodDeclaratorRest937 = new BitSet(new long[]{0x00000A0002000000L});
    public static final BitSet FOLLOW_43_in_interfaceMethodDeclaratorRest942 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_interfaceMethodDeclaratorRest944 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_interfaceMethodDeclaratorRest948 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeParameters_in_interfaceGenericMethodDecl960 = new BitSet(new long[]{0x7F80010000000010L});
    public static final BitSet FOLLOW_type_in_interfaceGenericMethodDecl963 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_40_in_interfaceGenericMethodDecl967 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_interfaceGenericMethodDecl970 = new BitSet(new long[]{0x0000120000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodDeclaratorRest_in_interfaceGenericMethodDecl980 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_voidInterfaceMethodDeclaratorRest992 = new BitSet(new long[]{0x0000080002000000L});
    public static final BitSet FOLLOW_43_in_voidInterfaceMethodDeclaratorRest995 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_voidInterfaceMethodDeclaratorRest997 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_voidInterfaceMethodDeclaratorRest1001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_constructorDeclaratorRest1013 = new BitSet(new long[]{0x0000082008000000L});
    public static final BitSet FOLLOW_43_in_constructorDeclaratorRest1016 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_constructorDeclaratorRest1018 = new BitSet(new long[]{0x0000002008000000L});
    public static final BitSet FOLLOW_methodBody_in_constructorDeclaratorRest1022 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_constantDeclarator1033 = new BitSet(new long[]{0x0000120000000000L});
    public static final BitSet FOLLOW_constantDeclaratorRest_in_constantDeclarator1035 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclarator_in_variableDeclarators1047 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_34_in_variableDeclarators1050 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclarator_in_variableDeclarators1052 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_Identifier_in_variableDeclarator1084 = new BitSet(new long[]{0x0000120000000000L});
    public static final BitSet FOLLOW_variableDeclaratorRest_in_variableDeclarator1088 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_variableDeclaratorRest1106 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_variableDeclaratorRest1108 = new BitSet(new long[]{0x0000120000000002L});
    public static final BitSet FOLLOW_44_in_variableDeclaratorRest1113 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_variableInitializer_in_variableDeclaratorRest1115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_variableDeclaratorRest1122 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_variableInitializer_in_variableDeclaratorRest1124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constantDeclaratorRest_in_constantDeclaratorsRest1144 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_34_in_constantDeclaratorsRest1147 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_constantDeclarator_in_constantDeclaratorsRest1149 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_41_in_constantDeclaratorRest1166 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_constantDeclaratorRest1168 = new BitSet(new long[]{0x0000120000000000L});
    public static final BitSet FOLLOW_44_in_constantDeclaratorRest1172 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_variableInitializer_in_constantDeclaratorRest1174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_variableDeclaratorId1186 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_41_in_variableDeclaratorId1189 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_variableDeclaratorId1191 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_arrayInitializer_in_variableInitializer1204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_variableInitializer1214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_arrayInitializer1226 = new BitSet(new long[]{0x7F80016200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer1229 = new BitSet(new long[]{0x0000004400000000L});
    public static final BitSet FOLLOW_34_in_arrayInitializer1232 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer1234 = new BitSet(new long[]{0x0000004400000000L});
    public static final BitSet FOLLOW_34_in_arrayInitializer1239 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_38_in_arrayInitializer1246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_modifier1262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_modifier1272 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_modifier1282 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_modifier1292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_modifier1302 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_modifier1312 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_modifier1322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_modifier1332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_51_in_modifier1342 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_modifier1352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_modifier1362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_modifier1372 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_packageOrTypeName1386 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_28_in_packageOrTypeName1389 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_packageOrTypeName1391 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_Identifier_in_enumConstantName1409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_typeName1425 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_packageOrTypeName_in_typeName1435 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_typeName1437 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_typeName1439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_type1450 = new BitSet(new long[]{0x0000020210000002L});
    public static final BitSet FOLLOW_typeArguments_in_type1453 = new BitSet(new long[]{0x0000020010000002L});
    public static final BitSet FOLLOW_28_in_type1458 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_type1460 = new BitSet(new long[]{0x0000020210000002L});
    public static final BitSet FOLLOW_typeArguments_in_type1463 = new BitSet(new long[]{0x0000020010000002L});
    public static final BitSet FOLLOW_41_in_type1471 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_type1473 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_primitiveType_in_type1480 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_41_in_type1483 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_type1485 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_set_in_primitiveType0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_variableModifier1573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_variableModifier1583 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_typeArguments1594 = new BitSet(new long[]{0xFF80000000000010L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments1596 = new BitSet(new long[]{0x0000000C00000000L});
    public static final BitSet FOLLOW_34_in_typeArguments1599 = new BitSet(new long[]{0xFF80000000000010L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments1601 = new BitSet(new long[]{0x0000000C00000000L});
    public static final BitSet FOLLOW_35_in_typeArguments1605 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeArgument1617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_typeArgument1622 = new BitSet(new long[]{0x0000000080000002L,0x0000000000000001L});
    public static final BitSet FOLLOW_set_in_typeArgument1625 = new BitSet(new long[]{0x7F80000000000010L});
    public static final BitSet FOLLOW_type_in_typeArgument1633 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList1647 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_34_in_qualifiedNameList1650 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList1652 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_65_in_formalParameters1666 = new BitSet(new long[]{0x7F82000000000010L,0x0000000000000084L});
    public static final BitSet FOLLOW_formalParameterDecls_in_formalParameters1668 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_formalParameters1671 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifier_in_formalParameterDecls1683 = new BitSet(new long[]{0x7F82000000000010L,0x0000000000000080L});
    public static final BitSet FOLLOW_type_in_formalParameterDecls1686 = new BitSet(new long[]{0x0000000000000012L,0x0000000000000008L});
    public static final BitSet FOLLOW_formalParameterDeclsRest_in_formalParameterDecls1688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest1701 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_34_in_formalParameterDeclsRest1704 = new BitSet(new long[]{0x7F82000000000010L,0x0000000000000080L});
    public static final BitSet FOLLOW_formalParameterDecls_in_formalParameterDeclsRest1706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_formalParameterDeclsRest1715 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest1717 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_methodBody1729 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_qualifiedName1740 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_28_in_qualifiedName1743 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_qualifiedName1745 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_integerLiteral_in_literal1762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FloatingPointLiteral_in_literal1772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CharacterLiteral_in_literal1782 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_StringLiteral_in_literal1792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_booleanLiteral_in_literal1802 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_literal1812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_integerLiteral0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_booleanLiteral0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_annotations1893 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_71_in_annotation1905 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_annotationName_in_annotation1907 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_annotation1910 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C0000F7L});
    public static final BitSet FOLLOW_elementValuePairs_in_annotation1912 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_annotation1915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_annotationName1929 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_28_in_annotationName1932 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_annotationName1934 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs1948 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_34_in_elementValuePairs1951 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C0000F3L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs1953 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_Identifier_in_elementValuePair1968 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_elementValuePair1970 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C0000F3L});
    public static final BitSet FOLLOW_elementValue_in_elementValuePair1974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_elementValue1986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_elementValue1993 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementValueArrayInitializer_in_elementValue2000 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_elementValueArrayInitializer2012 = new BitSet(new long[]{0x7F80016200000FD0L,0x00FCC0001C0000F3L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer2015 = new BitSet(new long[]{0x0000004400000000L});
    public static final BitSet FOLLOW_34_in_elementValueArrayInitializer2018 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C0000F3L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer2020 = new BitSet(new long[]{0x0000004400000000L});
    public static final BitSet FOLLOW_38_in_elementValueArrayInitializer2027 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_71_in_annotationTypeDeclaration2039 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_39_in_annotationTypeDeclaration2041 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_annotationTypeDeclaration2043 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_annotationTypeBody_in_annotationTypeDeclaration2045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_annotationTypeBody2057 = new BitSet(new long[]{0x7FFFE0C048000030L,0x0000000000000080L});
    public static final BitSet FOLLOW_annotationTypeElementDeclarations_in_annotationTypeBody2060 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_38_in_annotationTypeBody2064 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeElementDeclaration_in_annotationTypeElementDeclarations2077 = new BitSet(new long[]{0x7FFFE08048000032L,0x0000000000000080L});
    public static final BitSet FOLLOW_annotationTypeElementDeclaration_in_annotationTypeElementDeclarations2081 = new BitSet(new long[]{0x7FFFE08048000032L,0x0000000000000080L});
    public static final BitSet FOLLOW_modifier_in_annotationTypeElementDeclaration2096 = new BitSet(new long[]{0x7FFFE08048000030L,0x0000000000000080L});
    public static final BitSet FOLLOW_annotationTypeElementRest_in_annotationTypeElementDeclaration2100 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_annotationTypeElementRest2112 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_annotationMethodOrConstantRest_in_annotationTypeElementRest2114 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_annotationTypeElementRest2116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_annotationTypeElementRest2123 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_annotationTypeElementRest2125 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_annotationTypeElementRest2133 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_annotationTypeElementRest2135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_annotationTypeElementRest2143 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_annotationTypeElementRest2145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_annotationTypeElementRest2153 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_annotationTypeElementRest2155 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationMethodRest_in_annotationMethodOrConstantRest2168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationConstantRest_in_annotationMethodOrConstantRest2175 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_annotationMethodRest2188 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_annotationMethodRest2190 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_annotationMethodRest2192 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_defaultValue_in_annotationMethodRest2195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclarators_in_annotationConstantRest2212 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_72_in_defaultValue2227 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C0000F3L});
    public static final BitSet FOLLOW_elementValue_in_defaultValue2229 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_block2269 = new BitSet(new long[]{0x7FFFE1E24A000FF0L,0x00FCC0001CFDEAF3L});
    public static final BitSet FOLLOW_blockStatement_in_block2271 = new BitSet(new long[]{0x7FFFE1E24A000FF0L,0x00FCC0001CFDEAF3L});
    public static final BitSet FOLLOW_38_in_block2274 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_blockStatement2286 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_blockStatement2291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_blockStatement2300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifier_in_localVariableDeclaration2348 = new BitSet(new long[]{0x7F82000000000010L,0x0000000000000080L});
    public static final BitSet FOLLOW_type_in_localVariableDeclaration2365 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclarators_in_localVariableDeclaration2376 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_localVariableDeclaration2378 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_statement2390 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_statement2398 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_statement2400 = new BitSet(new long[]{0x0000000002000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_statement2403 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_statement2405 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_statement2409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_75_in_statement2417 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_statement2419 = new BitSet(new long[]{0x7FFFE1A24A000FF0L,0x00FCC0001CFDEAF3L});
    public static final BitSet FOLLOW_statement_in_statement2421 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_statement2431 = new BitSet(new long[]{0x7FFFE1A24A000FF0L,0x00FCC0001CFDEAF3L});
    public static final BitSet FOLLOW_statement_in_statement2433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_77_in_statement2443 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_statement2445 = new BitSet(new long[]{0x7F82012202000FD0L,0x00FCC0001C0000F3L});
    public static final BitSet FOLLOW_forControl_in_statement2447 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_statement2449 = new BitSet(new long[]{0x7FFFE1A24A000FF0L,0x00FCC0001CFDEAF3L});
    public static final BitSet FOLLOW_statement_in_statement2451 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_78_in_statement2459 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_statement2461 = new BitSet(new long[]{0x7FFFE1A24A000FF0L,0x00FCC0001CFDEAF3L});
    public static final BitSet FOLLOW_statement_in_statement2463 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_79_in_statement2471 = new BitSet(new long[]{0x7FFFE1A24A000FF0L,0x00FCC0001CFDEAF3L});
    public static final BitSet FOLLOW_statement_in_statement2473 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_78_in_statement2475 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_statement2477 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_statement2479 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_80_in_statement2487 = new BitSet(new long[]{0x0000002008000000L});
    public static final BitSet FOLLOW_block_in_statement2489 = new BitSet(new long[]{0x0000000000000000L,0x0000000020020000L});
    public static final BitSet FOLLOW_catches_in_statement2499 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_81_in_statement2501 = new BitSet(new long[]{0x0000002008000000L});
    public static final BitSet FOLLOW_block_in_statement2503 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_statement2513 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_81_in_statement2523 = new BitSet(new long[]{0x0000002008000000L});
    public static final BitSet FOLLOW_block_in_statement2525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_82_in_statement2541 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_statement2543 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_statement2545 = new BitSet(new long[]{0x0000004000000000L,0x0000000040000100L});
    public static final BitSet FOLLOW_switchBlockStatementGroups_in_statement2547 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_38_in_statement2549 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_51_in_statement2557 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_statement2559 = new BitSet(new long[]{0x0000002008000000L});
    public static final BitSet FOLLOW_block_in_statement2561 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_83_in_statement2569 = new BitSet(new long[]{0x7F80012202000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_statement2571 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_statement2574 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_84_in_statement2582 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_statement2584 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_statement2586 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_85_in_statement2594 = new BitSet(new long[]{0x0000000002000010L});
    public static final BitSet FOLLOW_Identifier_in_statement2596 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_statement2599 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_86_in_statement2607 = new BitSet(new long[]{0x0000000002000010L});
    public static final BitSet FOLLOW_Identifier_in_statement2609 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_statement2612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifyStatement_in_statement2625 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_statement2633 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statementExpression_in_statement2641 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_statement2643 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_statement2651 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_statement2653 = new BitSet(new long[]{0x7FFFE1A24A000FF0L,0x00FCC0001CFDEAF3L});
    public static final BitSet FOLLOW_statement_in_statement2655 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_87_in_modifyStatement2675 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_modifyStatement2677 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_modifyStatement2684 = new BitSet(new long[]{0x7F80016200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_modifyStatement2692 = new BitSet(new long[]{0x0000004400000000L});
    public static final BitSet FOLLOW_34_in_modifyStatement2705 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_modifyStatement2709 = new BitSet(new long[]{0x0000004400000000L});
    public static final BitSet FOLLOW_38_in_modifyStatement2728 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_88_in_updateStatement2758 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_updateStatement2760 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_retractStatement2792 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_retractStatement2794 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_90_in_epStatement2848 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_41_in_epStatement2850 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_StringLiteral_in_epStatement2854 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_epStatement2858 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_91_in_epStatement2884 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_41_in_epStatement2886 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_StringLiteral_in_epStatement2890 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_epStatement2894 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_92_in_epStatement2920 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_41_in_epStatement2922 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_StringLiteral_in_epStatement2926 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_epStatement2930 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_catches2972 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
    public static final BitSet FOLLOW_catchClause_in_catches2975 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
    public static final BitSet FOLLOW_93_in_catchClause2989 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_catchClause2991 = new BitSet(new long[]{0x7F82000000000010L,0x0000000000000080L});
    public static final BitSet FOLLOW_formalParameter_in_catchClause2993 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_catchClause2995 = new BitSet(new long[]{0x0000002008000000L});
    public static final BitSet FOLLOW_block_in_catchClause2997 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifier_in_formalParameter3008 = new BitSet(new long[]{0x7F82000000000010L,0x0000000000000080L});
    public static final BitSet FOLLOW_type_in_formalParameter3011 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameter3013 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups3027 = new BitSet(new long[]{0x0000000000000002L,0x0000000040000100L});
    public static final BitSet FOLLOW_switchLabel_in_switchBlockStatementGroup3041 = new BitSet(new long[]{0x7FFFE1A24A000FF2L,0x00FCC0001CFDEAF3L});
    public static final BitSet FOLLOW_blockStatement_in_switchBlockStatementGroup3043 = new BitSet(new long[]{0x7FFFE1A24A000FF2L,0x00FCC0001CFDEAF3L});
    public static final BitSet FOLLOW_94_in_switchLabel3056 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_constantExpression_in_switchLabel3058 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_switchLabel3060 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_switchLabel3067 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_enumConstantName_in_switchLabel3069 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_switchLabel3071 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_72_in_switchLabel3078 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_switchLabel3080 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_moreStatementExpressions3093 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_statementExpression_in_moreStatementExpressions3095 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_forVarControl_in_forControl3116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forInit_in_forControl3121 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_forControl3124 = new BitSet(new long[]{0x7F80012202000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_forControl3126 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_forControl3129 = new BitSet(new long[]{0x7F82012200000FD2L,0x00FCC0001C0000F3L});
    public static final BitSet FOLLOW_forUpdate_in_forControl3131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifier_in_forInit3169 = new BitSet(new long[]{0x7F82000000000010L,0x0000000000000080L});
    public static final BitSet FOLLOW_type_in_forInit3172 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclarators_in_forInit3174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_forInit3179 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifier_in_forVarControl3191 = new BitSet(new long[]{0x7F82000000000010L,0x0000000000000080L});
    public static final BitSet FOLLOW_type_in_forVarControl3194 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_forVarControl3196 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_forVarControl3198 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_forVarControl3200 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_forUpdate3211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_parExpression3224 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_parExpression3226 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_parExpression3228 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionList3245 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_34_in_expressionList3248 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_expressionList3250 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_expression_in_statementExpression3266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_constantExpression3278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_expression3290 = new BitSet(new long[]{0x0000100A00000002L,0x0000007F80000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_expression3293 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_expression3295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_assignmentOperator3309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_95_in_assignmentOperator3319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_96_in_assignmentOperator3329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_97_in_assignmentOperator3339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_assignmentOperator3349 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_assignmentOperator3359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_100_in_assignmentOperator3369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_101_in_assignmentOperator3379 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_102_in_assignmentOperator3389 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_assignmentOperator3399 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_assignmentOperator3401 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_assignmentOperator3403 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_assignmentOperator3413 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_assignmentOperator3415 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_assignmentOperator3417 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_assignmentOperator3427 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_assignmentOperator3429 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_assignmentOperator3431 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_assignmentOperator3433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalOrExpression_in_conditionalExpression3449 = new BitSet(new long[]{0x8000000000000002L});
    public static final BitSet FOLLOW_63_in_conditionalExpression3453 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_conditionalExpression3455 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_conditionalExpression3457 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_conditionalExpression3459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression3478 = new BitSet(new long[]{0x0000000000000002L,0x0000008000000000L});
    public static final BitSet FOLLOW_103_in_conditionalOrExpression3482 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression3484 = new BitSet(new long[]{0x0000000000000002L,0x0000008000000000L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression3503 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
    public static final BitSet FOLLOW_104_in_conditionalAndExpression3507 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression3509 = new BitSet(new long[]{0x0000000000000002L,0x0000010000000000L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression3528 = new BitSet(new long[]{0x0000000000000002L,0x0000020000000000L});
    public static final BitSet FOLLOW_105_in_inclusiveOrExpression3532 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression3534 = new BitSet(new long[]{0x0000000000000002L,0x0000020000000000L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression3553 = new BitSet(new long[]{0x0000000000000002L,0x0000040000000000L});
    public static final BitSet FOLLOW_106_in_exclusiveOrExpression3557 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression3559 = new BitSet(new long[]{0x0000000000000002L,0x0000040000000000L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression3578 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_36_in_andExpression3582 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression3584 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression3603 = new BitSet(new long[]{0x0000000000000002L,0x0000180000000000L});
    public static final BitSet FOLLOW_set_in_equalityExpression3607 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression3615 = new BitSet(new long[]{0x0000000000000002L,0x0000180000000000L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression3634 = new BitSet(new long[]{0x0000000000000002L,0x0000200000000000L});
    public static final BitSet FOLLOW_109_in_instanceOfExpression3637 = new BitSet(new long[]{0x7F80000000000010L});
    public static final BitSet FOLLOW_type_in_instanceOfExpression3639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression3657 = new BitSet(new long[]{0x0000000A00000002L});
    public static final BitSet FOLLOW_relationalOp_in_relationalExpression3661 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression3663 = new BitSet(new long[]{0x0000000A00000002L});
    public static final BitSet FOLLOW_33_in_relationalOp3679 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_relationalOp3681 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_relationalOp3685 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_relationalOp3687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_relationalOp3691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_relationalOp3695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression3712 = new BitSet(new long[]{0x0000000A00000002L});
    public static final BitSet FOLLOW_shiftOp_in_shiftExpression3716 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression3718 = new BitSet(new long[]{0x0000000A00000002L});
    public static final BitSet FOLLOW_33_in_shiftOp3742 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_shiftOp3744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_shiftOp3748 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_shiftOp3750 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_shiftOp3752 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_shiftOp3756 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_shiftOp3758 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression3776 = new BitSet(new long[]{0x0000000000000002L,0x0000C00000000000L});
    public static final BitSet FOLLOW_set_in_additiveExpression3780 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression3788 = new BitSet(new long[]{0x0000000000000002L,0x0000C00000000000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression3807 = new BitSet(new long[]{0x0000000020000002L,0x0003000000000000L});
    public static final BitSet FOLLOW_set_in_multiplicativeExpression3811 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression3825 = new BitSet(new long[]{0x0000000020000002L,0x0003000000000000L});
    public static final BitSet FOLLOW_110_in_unaryExpression3845 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression3847 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_111_in_unaryExpression3855 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression3857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_114_in_unaryExpression3867 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_primary_in_unaryExpression3869 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_115_in_unaryExpression3879 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_primary_in_unaryExpression3881 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression3891 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_116_in_unaryExpressionNotPlusMinus3910 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus3912 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_117_in_unaryExpressionNotPlusMinus3921 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus3923 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_unaryExpressionNotPlusMinus3933 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_unaryExpressionNotPlusMinus3943 = new BitSet(new long[]{0x0000020010000002L,0x000C000000000000L});
    public static final BitSet FOLLOW_selector_in_unaryExpressionNotPlusMinus3945 = new BitSet(new long[]{0x0000020010000002L,0x000C000000000000L});
    public static final BitSet FOLLOW_set_in_unaryExpressionNotPlusMinus3948 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_castExpression3971 = new BitSet(new long[]{0x7F80000000000010L});
    public static final BitSet FOLLOW_primitiveType_in_castExpression3973 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_castExpression3975 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_unaryExpression_in_castExpression3977 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_castExpression3986 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_type_in_castExpression3989 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_castExpression3993 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_castExpression3996 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_castExpression3998 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_primary4015 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_primary4025 = new BitSet(new long[]{0x0000000000000010L,0x0040000000000001L});
    public static final BitSet FOLLOW_explicitGenericInvocationSuffix_in_primary4036 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_118_in_primary4040 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_primary4042 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_118_in_primary4053 = new BitSet(new long[]{0x0000020010000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_primary4056 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_primary4058 = new BitSet(new long[]{0x0000020010000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary4063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_primary4075 = new BitSet(new long[]{0x0000000010000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_superSuffix_in_primary4077 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_epStatement_in_primary4087 = new BitSet(new long[]{0x0000020010000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_primary4090 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_primary4092 = new BitSet(new long[]{0x0000020010000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary4097 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primary4109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_119_in_primary4119 = new BitSet(new long[]{0x7F80000200000010L});
    public static final BitSet FOLLOW_creator_in_primary4121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_primary4133 = new BitSet(new long[]{0x0000020010000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_primary4138 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_primary4140 = new BitSet(new long[]{0x0000020010000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary4145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_primary4157 = new BitSet(new long[]{0x0000020010000000L});
    public static final BitSet FOLLOW_41_in_primary4160 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_primary4162 = new BitSet(new long[]{0x0000020010000000L});
    public static final BitSet FOLLOW_28_in_primary4166 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_primary4168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_primary4178 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_primary4180 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_primary4182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_identifierSuffix4194 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_identifierSuffix4196 = new BitSet(new long[]{0x0000020010000000L});
    public static final BitSet FOLLOW_28_in_identifierSuffix4200 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_identifierSuffix4202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_identifierSuffix4208 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_identifierSuffix4210 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_identifierSuffix4212 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix4225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_identifierSuffix4235 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_identifierSuffix4237 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_identifierSuffix4247 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_explicitGenericInvocation_in_identifierSuffix4249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_identifierSuffix4259 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_118_in_identifierSuffix4261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_identifierSuffix4271 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_identifierSuffix4273 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix4275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_identifierSuffix4285 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_119_in_identifierSuffix4287 = new BitSet(new long[]{0x0000000200000010L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_identifierSuffix4290 = new BitSet(new long[]{0x0000000200000010L});
    public static final BitSet FOLLOW_innerCreator_in_identifierSuffix4294 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_creator4306 = new BitSet(new long[]{0x7F80000200000010L});
    public static final BitSet FOLLOW_createdName_in_creator4309 = new BitSet(new long[]{0x0000020000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_arrayCreatorRest_in_creator4320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classCreatorRest_in_creator4324 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_createdName4336 = new BitSet(new long[]{0x0000000210000002L});
    public static final BitSet FOLLOW_typeArguments_in_createdName4338 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_28_in_createdName4350 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_createdName4352 = new BitSet(new long[]{0x0000000210000002L});
    public static final BitSet FOLLOW_typeArguments_in_createdName4354 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_primitiveType_in_createdName4365 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_innerCreator4377 = new BitSet(new long[]{0x0000020000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_classCreatorRest_in_innerCreator4379 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_arrayCreatorRest4390 = new BitSet(new long[]{0x7F80052200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_42_in_arrayCreatorRest4404 = new BitSet(new long[]{0x0000022000000000L});
    public static final BitSet FOLLOW_41_in_arrayCreatorRest4407 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_arrayCreatorRest4409 = new BitSet(new long[]{0x0000022000000000L});
    public static final BitSet FOLLOW_arrayInitializer_in_arrayCreatorRest4413 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_arrayCreatorRest4427 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_arrayCreatorRest4429 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_41_in_arrayCreatorRest4432 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_arrayCreatorRest4434 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_arrayCreatorRest4436 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_41_in_arrayCreatorRest4441 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_arrayCreatorRest4443 = new BitSet(new long[]{0x0000020000000002L});
    public static final BitSet FOLLOW_arguments_in_classCreatorRest4466 = new BitSet(new long[]{0x0000002380000002L});
    public static final BitSet FOLLOW_classBody_in_classCreatorRest4468 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitGenericInvocation4481 = new BitSet(new long[]{0x0000000000000010L,0x0000000000000001L});
    public static final BitSet FOLLOW_explicitGenericInvocationSuffix_in_explicitGenericInvocation4483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_nonWildcardTypeArguments4495 = new BitSet(new long[]{0x7F80000000000010L});
    public static final BitSet FOLLOW_typeList_in_nonWildcardTypeArguments4497 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_nonWildcardTypeArguments4499 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_explicitGenericInvocationSuffix4511 = new BitSet(new long[]{0x0000000010000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_superSuffix_in_explicitGenericInvocationSuffix4513 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_explicitGenericInvocationSuffix4520 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_explicitGenericInvocationSuffix4522 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_selector4534 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_selector4536 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_selector4539 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_selector4548 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_118_in_selector4550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_selector4557 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_selector4559 = new BitSet(new long[]{0x0000000010000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_superSuffix_in_selector4561 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_selector4568 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_119_in_selector4570 = new BitSet(new long[]{0x0000000200000010L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_selector4573 = new BitSet(new long[]{0x0000000200000010L});
    public static final BitSet FOLLOW_innerCreator_in_selector4577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_selector4584 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_selector4586 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_selector4588 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_superSuffix4600 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_superSuffix4607 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_superSuffix4609 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_superSuffix4612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_arguments4628 = new BitSet(new long[]{0x7F82012200000FD0L,0x00FCC0001C0000F7L});
    public static final BitSet FOLLOW_expressionList_in_arguments4630 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_arguments4633 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotations_in_synpred1_Java70 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodDeclaration_in_synpred38_Java577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldDeclaration_in_synpred39_Java582 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_synpred85_Java1389 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_synpred85_Java1391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_synpred120_Java1893 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_synpred135_Java2123 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_synpred135_Java2125 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_synpred137_Java2133 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_synpred137_Java2135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_synpred139_Java2143 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_25_in_synpred139_Java2145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_synpred144_Java2286 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_synpred145_Java2291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_synpred150_Java2431 = new BitSet(new long[]{0x7FFFE1A24A000FF0L,0x00FCC0001CFDEAF3L});
    public static final BitSet FOLLOW_statement_in_synpred150_Java2433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred155_Java2499 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_81_in_synpred155_Java2501 = new BitSet(new long[]{0x0000002008000000L});
    public static final BitSet FOLLOW_block_in_synpred155_Java2503 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred156_Java2513 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_synpred178_Java3056 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_constantExpression_in_synpred178_Java3058 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_synpred178_Java3060 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_synpred179_Java3067 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_enumConstantName_in_synpred179_Java3069 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_synpred179_Java3071 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forVarControl_in_synpred181_Java3116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifier_in_synpred186_Java3169 = new BitSet(new long[]{0x7F82000000000010L,0x0000000000000080L});
    public static final BitSet FOLLOW_type_in_synpred186_Java3172 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclarators_in_synpred186_Java3174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOperator_in_synpred189_Java3293 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_synpred189_Java3295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_synpred200_Java3413 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_synpred200_Java3415 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_synpred200_Java3417 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_relationalOp_in_synpred210_Java3661 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_shiftExpression_in_synpred210_Java3663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_shiftOp_in_synpred214_Java3716 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_additiveExpression_in_synpred214_Java3718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_synpred216_Java3748 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_synpred216_Java3750 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_synpred216_Java3752 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_synpred228_Java3933 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_synpred232_Java3971 = new BitSet(new long[]{0x7F80000000000010L});
    public static final BitSet FOLLOW_primitiveType_in_synpred232_Java3973 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_synpred232_Java3975 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_unaryExpression_in_synpred232_Java3977 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_synpred233_Java3989 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_synpred237_Java4056 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_synpred237_Java4058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred238_Java4063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_synpred241_Java4090 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_synpred241_Java4092 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred242_Java4097 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_synpred246_Java4138 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Identifier_in_synpred246_Java4140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred247_Java4145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_synpred253_Java4208 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_synpred253_Java4210 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_synpred253_Java4212 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_synpred269_Java4432 = new BitSet(new long[]{0x7F80012200000FD0L,0x00FCC0001C000073L});
    public static final BitSet FOLLOW_expression_in_synpred269_Java4434 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_synpred269_Java4436 = new BitSet(new long[]{0x0000000000000002L});

}
