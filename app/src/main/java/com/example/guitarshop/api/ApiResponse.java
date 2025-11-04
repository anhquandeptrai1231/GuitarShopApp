package com.example.guitarshop.api;


    public class ApiResponse<T> {
        private boolean success;
        private T data;

        public boolean isSuccess() {
            return success;
        }

        public T getData() {
            return data;
        }
}
