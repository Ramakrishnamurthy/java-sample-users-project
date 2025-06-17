used on maven setup 3.9.9

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
