import { createAsyncThunk } from "@reduxjs/toolkit";
// import amsApi from "../../api/amsApi";

// const storedToken = localStorage.getItem("token");
export const userLogin = createAsyncThunk("auth/userLogin");
