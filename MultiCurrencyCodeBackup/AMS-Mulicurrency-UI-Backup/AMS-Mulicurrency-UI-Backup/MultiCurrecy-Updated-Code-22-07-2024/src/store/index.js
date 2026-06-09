import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./authFeatures/authSlice";

const store = configureStore({
  reducer: {
    auth: authReducer,
  },
});
export default store;
