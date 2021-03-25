package datalayer 

import (
	// "github.com/pkg/errors"
	"database/sql"
	// "encoding/json"
	_ "github.com/mattn/go-sqlite3"
	"net/http"
	"fmt"
	// "strconv"
)

type DataProvider struct {
	TableName string
	Db *sql.DB
}

type Record struct {
	Key  int
	Name string
}

func (dataProvider *DataProvider) GetList(w http.ResponseWriter) ([]Record, error) {
	rows, err := dataProvider.Db.Query(fmt.Sprintf("select * from %s", dataProvider.TableName))
	
	// Retrieve all lists from the query
	res := make([]Record, 0)
	if dataProvider.CheckFatal(err, w) { return res, err }

	for rows.Next() {
		record := Record{}
		err := rows.Scan(&record.Key, &record.Name)
		if dataProvider.CheckFatal(err, w) { return res, err }
		res = append(res, record)
	}

	return res, nil
}

func (dataProvider *DataProvider) FindName(w http.ResponseWriter, targetName string) (Record, error) {
	queryInput := fmt.Sprintf("select * from %s where p_name = $1", dataProvider.TableName)
	
	rows, err := dataProvider.Db.Query(queryInput, targetName)
	
	record := Record{}
	if dataProvider.CheckFatal(err, w) { return record, err }
	rows.Next()
	scan_err := rows.Scan(&record.Key, &record.Name)
	if dataProvider.CheckFatal(scan_err, w) { return record, scan_err }
	return record, nil
}

func (dataProvider *DataProvider) FindKey(w http.ResponseWriter, targetKey string) (Record, error) {
	queryInput := fmt.Sprintf("select * from %s where p_key = $1", dataProvider.TableName)
	
	rows, err := dataProvider.Db.Query(queryInput, targetKey) // "select * from  where p_key = $1",  targetKey)

	record := Record{}
	if dataProvider.CheckFatal(err, w) { return record, err }
	rows.Next()
	scan_err := rows.Scan(&record.Key, &record.Name)
	if dataProvider.CheckFatal(scan_err, w) { return record, scan_err }
	return record, nil
}


func (dataProvider *DataProvider) CheckFatal(err error, w http.ResponseWriter) bool {
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		return true
	}
	return false
}