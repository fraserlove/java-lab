trap "kill 0" EXIT
PORT=$((12000 + ($RANDOM % 1000)))  # avoids conflicts with other students
SEED=43
(java -cp "$TESTDIR/../javax.json-1.0.jar:." GameServerMain $PORT $SEED > /dev/null 2>&1 &)
sleep 2
npx --yes newman run https://cs5001-p2.dynv6.net/3.json --env-var "base_url=localhost" --env-var "port=$PORT"