package main

import (
	_ "github.com/pkg/errors"
	"database/sql"
	 "encoding/json"
	_ "github.com/lib/pq"
	_ "github.com/mattn/go-sqlite3"
	"log"
	"net/http"
	"os"
	"person/datalayer"
)

var (
	logger *remoteLogger
)

type RequestManager struct {
	Person datalayer.DataProvider 
}

// ConnextDB connects to a postgres database.
// it returns a database handle.
func ConnectDb(dbName string) *sql.DB {
	db, err := sql.Open("sqlite3", dbName)

	if err != nil {
		log.Fatal(err)
	}

	return db
}


// getLists retrieves all the lists from the database.
// It returns a slice of List structs.


// listHandler manages requests with regards to the lists.
// A GET request to /list will retrieve all the lists.
// A GET request to /list/<id> will retrieve all the tasks of the list with id <id>.
func (rm *RequestManager) ListHandler(w http.ResponseWriter, r *http.Request) {
	person := &rm.Person;
	if r.Method != "GET" {
		w.WriteHeader(http.StatusInternalServerError)
	}

	logger.log("GET /list " + r.URL.Path)

	data, err := person.GetList(w)
	if(err == nil) {
		json.NewEncoder(w).Encode(&data)
	}
}


func (rm *RequestManager) FindHandler(w http.ResponseWriter, r *http.Request)  {
	
	

	provider := &rm.Person;
	if r.Method != "GET" {
		w.WriteHeader(http.StatusInternalServerError)
		return;
	}

	keys, k_ok  := r.URL.Query()["key"]
	names, n_ok := r.URL.Query()["name"]

	encoder     := json.NewEncoder(w);
	
	if n_ok && len(names) > 0 {
		logger.log("GET /find " + r.URL.Path  + " " +  names[0])
		data, err := provider.FindName(w, names[0])
		if err == nil {		
			encoder.Encode(&data)	
		} else {
			encoder.Encode(nil)
		}
	} else if k_ok && len(keys) > 0 {
		logger.log("GET /find " + r.URL.Path + " " + keys[0])
		data, err := provider.FindKey(w, keys[0])
		if err == nil {		
			encoder.Encode(&data)	
		} else {
			encoder.Encode(nil)
		}
	}
}

func handlers(dbName string) *http.ServeMux {
	sql := ConnectDb(dbName)
	dataProvider := datalayer.DataProvider{TableName: "person", Db: sql} 
	rm  := RequestManager{Person: dataProvider}
	mux := http.NewServeMux()
	mux.Handle("/list", http.HandlerFunc(rm.ListHandler))
	mux.Handle("/find", http.HandlerFunc(rm.FindHandler))
	return mux
}

func main() {
	port   := ":" + os.Args[1]
	dbName := os.Args[2]
	logger = RemoteLogger("restrequest");
	err := http.ListenAndServe(port, handlers(dbName))
	if err != nil {
		log.Fatal(err)
	}
}
