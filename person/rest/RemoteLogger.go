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
}

