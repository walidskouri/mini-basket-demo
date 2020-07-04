db.createUser(
    {
        user: "walid",
        pwd: "walid",
        roles: [
            {
                role: "readWrite",
                db: "demo"
            }
        ]
    }
);