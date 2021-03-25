package main

import (
	"github.com/segmentio/kafka-go"
	"context"
	"time"
	"fmt"
)

// var (
// 	singleLogger *remoteLogger
// )

type remoteLogger struct {
	kafkaConnection *kafka.Conn
}

func RemoteLogger(topic string) *remoteLogger {

	partition := 0
	conn, err := kafka.DialLeader(context.Background(), "tcp", "localhost:9092", topic, partition);
	if err != nil {
		fmt.Println("err är \n", err)
		return nil
	}

	singleLogger  := &remoteLogger {
		kafkaConnection: conn,
	}

	return singleLogger;
}


func (r *remoteLogger) log(data string) {

	r.kafkaConnection.SetWriteDeadline(time.Now().Add(10*time.Second))
	fmt.Printf("deadline set")
	r.kafkaConnection.WriteMessages(
		kafka.Message{Value: []byte(data)},
	)

	// r.kafkaConnection.WriteMessages(
	// 	kafka.Message{Value: []byte("four!")},
	// 	kafka.Message{Value: []byte("five!")},
	// 	kafka.Message{Value: []byte("siz!")},
	// )
}

// func (r *remoteLogger) Get(key string) (string, error) {
// 	r.mu.RLock()
// 	defer r.mu.RUnlock()
// 	item, ok := r.items[key]
// 	if !ok {
// 		return "", fmt.Errorf("The '%s' is not presented", key)
// 	}
// 	return item, nil
// }


// type RemoteLogger struct {
// 	kafkaLogger kafka
// } 

// func logo(str string) {
// 	topic := "restrequest"
// 	partition := 0

// 	conn, _ := kafka.DialLeader(context.Background(), "tcp", "localhost:9092", topic, partition)

// 	conn.SetWriteDeadline(time.Now().Add(10*time.Second))
// 	conn.WriteMessages(
// 		kafka.Message{Value: []byte("one!")},
// 		kafka.Message{Value: []byte("two!")},
// 		kafka.Message{Value: []byte("three!")},
// 	)

// 	conn.WriteMessages(
// 		kafka.Message{Value: []byte("four!")},
// 		kafka.Message{Value: []byte("five!")},
// 		kafka.Message{Value: []byte("siz!")},
// 	)



// 	conn.Close()
// }
