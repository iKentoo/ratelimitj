
set -e
if [ "${VERSION}" = "" ]; then
   echo "set VERSION" && false
fi

if [ "${REPO_ID}" = "" ]; then
   echo "set REPO_ID" && false
fi

if [ "${REPO_URL}" = "" ]; then
   echo "set REPO_URL" && false
fi

./gradlew build install -Pversion=${VERSION}

for project in ratelimitj-*; do
if [ "${project}" = *test* ]; then
  continue
fi
echo ${project}
pom=$(find ${project} -name pom-default.xml)
jar=$(find ${project} -name ${project}-${VERSION}.jar)

mvn deploy:deploy-file -DpomFile=${pom} \
  -Dfile=${jar} \
  -DrepositoryId=${REPO_ID} \
  -Durl=${REPO_URL}
  done