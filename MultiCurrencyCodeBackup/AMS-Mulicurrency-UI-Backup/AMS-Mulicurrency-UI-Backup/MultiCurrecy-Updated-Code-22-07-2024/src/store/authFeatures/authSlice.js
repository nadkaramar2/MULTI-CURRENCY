import { createSlice } from "@reduxjs/toolkit";
import { userLogin } from "./authActions";

const initialState = {
  loading: false,
  authInfo: localStorage.getItem("authInfo")
    ? JSON.parse(localStorage.getItem("authInfo"))
    : null,
  error: null,
  success: false,
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    logout: (state) => {
      localStorage.removeItem("authInfo"); // deletes token from storage
      state.loading = false;
      state.authInfo = null;
      state.userToken = null;
      state.error = null;
    },
  },
  extraReducers: {
    // userLogin
    [userLogin.pending]: (state) => {
      state.loading = true;
      state.error = null;
    },
    [userLogin.fulfilled]: (state, { payload }) => {
      state.loading = false;
      state.authInfo = payload;
      state.success = true;
    },
    [userLogin.rejected]: (state, { payload }) => {
      state.loading = false;
      state.error = payload;
    },
  },
});
// export actions
export const { logout } = userSlice.actions;
export default userSlice.reducer;
