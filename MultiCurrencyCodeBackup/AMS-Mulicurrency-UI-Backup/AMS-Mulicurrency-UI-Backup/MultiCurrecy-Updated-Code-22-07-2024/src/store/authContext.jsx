import React, { useState, useEffect, useCallback } from "react";
let SignoutTimer;
const AuthContext = React.createContext({
  token: "",
  refreshToken: "",
  isAuthenticated: false,
  Signin: () => {},
  Signout: () => {},
});

const calculateRemainingTime = (expirationTime) => {
  const currentTime = new Date().getTime();
  const adjustedExpirationTime = new Date(expirationTime).getTime();
  return adjustedExpirationTime - currentTime;
};

const retrieveStoredToken = () => {
  const storedToken = localStorage.getItem("token");
  const storedRefreshToken = localStorage.getItem("refreshToken");
  const storedExpirationTime = localStorage.getItem("expirationTime");
  const remainingTime = calculateRemainingTime(storedExpirationTime);

  if (remainingTime <= 3600) {
    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("expirationTime");
    return null;
  }
  return {
    token: storedToken,
    refreshToken: storedRefreshToken,
    duration: remainingTime,
  };
};

export const AuthContextProvider = (props) => {
  const tokenData = retrieveStoredToken();
  let initialToken;
  let initialRefreshToken;

  if (tokenData) {
    initialToken = tokenData.token;
    initialRefreshToken = tokenData.refreshToken;
  }

  const [token, setToken] = useState(initialToken);
  const [refreshToken, setRefreshToken] = useState(initialRefreshToken);
  const userIsAuthenticated = !token;

  const SignoutHandler = useCallback(() => {
    setToken(null);
    setRefreshToken(null);
    localStorage.clear();
    if (SignoutTimer) {
      clearTimeout(SignoutTimer);
    }
  }, []);

  const udatakey = (token, expirationTime) => {
    setToken(token);
    localStorage.setItem("token", token);
    localStorage.setItem("refreshToken", refreshToken);
    localStorage.setItem("expirationTime", expirationTime);
    const remainingTime = calculateRemainingTime(expirationTime);
    SignoutTimer = setTimeout(SignoutHandler, remainingTime);
  };
  useEffect(() => {
    if (tokenData) {
      SignoutTimer = setTimeout(SignoutHandler, tokenData.duration);
    }
  }, [tokenData, SignoutHandler]);
  const contextValue = {
    token: token,
    refreshToken: refreshToken,
    isAuthenticated: userIsAuthenticated,
    Signin: udatakey,
    Signout: SignoutHandler,
  };
  return (
    <AuthContext.Provider value={contextValue}>
      {props.children}
    </AuthContext.Provider>
  );
};

export default AuthContext;
