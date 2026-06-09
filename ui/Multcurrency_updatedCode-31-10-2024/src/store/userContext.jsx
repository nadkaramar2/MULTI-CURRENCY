import React, { useState } from "react";
const UserContext = React.createContext({
  userRole: "-",
  userControl: "-",
  userRefreshToken: "-",
  setUserRole: () => {},
  setUserControl: () => {},
  setUserRefreshToken: () => {},
});
const retrieveStoredData = () => {
  const storedRole = localStorage.getItem("code");
  const storedControl = localStorage.getItem("Control");

  return {
    userRole: storedRole,
    userControl: storedControl,
  };
};
export const UserContextProvider = (props) => {
  const storedData = retrieveStoredData();
  var initialRole;

  var initialControl;

  if (storedData) {
    initialRole = storedData.userRole;
    initialControl = storedData.userControl;
  }
  const [userRole, setUserRole] = useState(initialRole);
  const [userControl, setUserControl] = useState(initialControl);
  const userRoleHandler = (userRole) => {
    setUserRole(userRole);
    localStorage.setItem("code", userRole);
  };
  const userControlHandler = (userControl) => {
    setUserControl(userControl);
    localStorage.setItem("control", userControl);
  };
  const contextValue = {
    userRole: userRole,
    userControl: userControl,
    setUserRole: userRoleHandler,
    setUserControl: userControlHandler,
  };
  return (
    <UserContext.Provider value={contextValue}>
      {props.children}
    </UserContext.Provider>
  );
};

export default UserContext;
