var MongoClient = require('mongodb').MongoClient;
var ObjectID = require('mongodb').ObjectID;
var url = "mongodb://localhost:27017/"; //host and db
var file = require('./data/retaurants.json');

MongoClient.connect(url, { useNewUrlParser: true, useUnifiedTopology: true }, async function (err, db) {
    var dbo = db.db("Resturant"); //db
    await file.map(elem => {
        elem._id = ObjectID(elem._id)

        dbo.collection("Resturant").insertOne(elem, function (err, res) { //collection
            if (err) throw err;
        });
    })
    console.log("done")

});
