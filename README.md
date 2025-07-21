Authentication

<img width="1132" height="825" alt="Screenshot 2025-07-21 at 5 01 55 PM" src="https://github.com/user-attachments/assets/842a24b2-f57e-4b63-bbc3-e9b5d7233897" />



And the Endpoint

<img width="1132" height="825" alt="Screenshot 2025-07-21 at 5 01 55 PM" src="https://github.com/user-attachments/assets/0a2b6016-4865-44c4-b0f4-9407ebfc299b" />







used on maven setup 3.9.9


Commands to run local::

mvn clean install
java -jar jarname

springboot-java7-style project


✅ 1. Create a User
bash
CopyEdit
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Rama","email":"rama@example.com"}'

✅ 2. Get All Users
bash
CopyEdit
curl http://localhost:8080/users

✅ 3. Get User by ID
bash
CopyEdit
curl http://localhost:8080/users/1

✅ 4. Update User by ID
bash
CopyEdit
curl -X PUT http://localhost:8080/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Rama","email":"updated@example.com"}'

✅ 5. Delete User by ID
bash
CopyEdit
curl -X DELETE http://localhost:8080/users/1

✅ 6. Search Users by Name (Optional Filter Logic if Added)
bash
CopyEdit
curl http://localhost:8080/users/search?keyword=Rama
(Assumes you add a custom filtering endpoint later, optional)




File Operations to create , read and delete a file

# Create a file
curl -X POST "http://localhost:8080/file/create?filename=test.txt&content=HelloWorld"

# Read the file
curl "http://localhost:8080/file/read?filename=test.txt"

# Delete the file
curl -X DELETE "http://localhost:8080/file/delete?filename=test.txt"

