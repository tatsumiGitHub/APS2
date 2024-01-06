.SUFFIXES: .java .class

JAVAC	= javac -encoding utf-8
JAVA	= java
SRC	= $(wildcard *.java)
CLASS	= $(SRC:.java=.class)
PROGRAM	= $(shell pwd)
MAIN	= Main

all: $(CLASS)

test: $(CLASS)
	$(JAVA) $(MAIN)

$(CLASS): $(SRC)

.java.class:
	$(JAVAC) $<

clean:;	\rm -f *.class */*.class */*/*.class */*/*/*.class \
		*~ */*~ */*/*~ */*/*/*~ \
		log/*

jar:; jar -cvfm AutomaticPlaybackSystemGUI_2.jar manifest.mf \
		*.class \
		*/*.class \
		*/*/*.class \
		*/*/*/*.class \

zip:;
	zip -r -o APS_2_GUI.zip \
		*.java \
		*/*.java \
		*/*/*.java \
		*/*/*/*.java \
		configs/setup.config \
		src/* \
		icon/* \
		img/* \
		log/ \
		manifest.mf \
		Makefile \
