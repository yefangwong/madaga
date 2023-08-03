[![SonarCloud Bugs](https://sonarcloud.io/api/project_badges/measure?project=yefangwong_madaga&metric=bugs)](https://sonarcloud.io/project/issues?resolved=false&types=BUG&id=yefangwong_madaga)
[![SonarCloud Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=yefangwong_madaga&metric=vulnerabilities)](https://sonarcloud.io/project/issues?resolved=false&types=VULNERABILITY&id=yefangwong_madaga)
[![SonarCloud Code Smells](https://sonarcloud.io/api/project_badges/measure?project=yefangwong_madaga&metric=code_smells)](https://sonarcloud.io/project/issues?resolved=false&types=VULNERABILITY&id=yefangwong_madaga)

# Madaga
Base on CSP (Cornelius Service Platform).
This was the platform roadmap built at June, 2012.
![Cornelius Architecture](https://user-images.githubusercontent.com/9351189/187391339-75a2c098-6ca7-4c43-ae61-bd5c4cd1fa5c.png)

# Install Java Development Kit
To see if previously installed

```javac -version```

Download Java SE

http://www.oracle.com/technetwork/java/javase/downloads/index.html

# Install Stanford CoreNLP
I don't have a Debian-based Linux environment available so this is untested, but it should be as follows:</br>
Download CoreNLP 4.5.4 and Unzip it somewhere:</br>
(Debian-based Linux and MacOS X)

```cd /usr/local/
mkdir StanfordCoreNLP
cd StanfordCoreNLP
curl -O -L http://nlp.stanford.edu/software/stanford-corenlp-latest.zip
unzip stanford-corenlp-latest.zip
cd stanford-corenlp-4.5.4
```
The root folder is then, for example:
```
/usr/local/StanfordCoreNLP/stanford-corenlp-4.5.4
```
Then download Chinese and English models to the root folder above:
```
cd /usr/local/StanfordCoreNLP/stanford-corenlp-4.5.4
wget http://nlp.stanford.edu/software/stanford-corenlp-4.5.4-models-chinese.jar
wget http://nlp.stanford.edu/software/stanford-corenlp-4.5.4-models-english.jar
```
Add all the .jar files to the CLASSPATH and the root folder to CORENLP_HOME
# MacOSX
In MacOSX we don’t have the ‘realpath’ module so install as a part of GNU coreutils with homebrew
```
brew install coreutils
```
In Apple M1 chip:
```
arch -arm64 brew install coreutils
```
add the following to ~/.bash_profile for user installation
```
CORENLP_HOME="/usr/local/StanfordCoreNLP/stanford-corenlp-4.5.4"
for file in `find $CORENLP_HOME -name "*.jar"`; do export
CLASSPATH="$CLASSPATH:`realpath $file`"; done
```
Now we can run the fowllowing command to activate new classpath setting
```
source ~/.bash_profile
```
# Running Stanford CoreNLP Server
Run a NLP server with chinese model
# MacOSX
```
cd $CORENLP_HOME
java -Xmx4g -cp "*" edu.stanford.nlp.pipeline.StanfordCoreNLPServer -serverProperties StanfordCoreNLP-chinese.properties -port 9000 -timeout 15000
```
# Build
java SE 8 : <br/>
```$ mvn clean install -P jdk8``` <br/>

java SE 11 : <br/>
```$ mvn clean install -P jdk11```
</p>

# Coding Standard
Because this project is written by Java language so I decide to follow this coding standard and Google is one of the best IT company in the world, so I decide to follow their coding standard such as there are 100 columns per row:

https://google.github.io/styleguide/javaguide.html

Builder

Mark Wong
