import React, { useEffect } from "react";
import { useLocation } from "react-router-dom";

// toast
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
// routes
import MainRoute from "./routes/Routes";
const App = () => {
  const { pathname } = useLocation();
  useEffect(() => {
    window.scrollTo({
      top: 0,
      left: 0,
      behavior: "smooth",
    });
  }, [pathname]);

  return (
    <React.StrictMode>
      {/* routes */}
      <MainRoute />
      {/* tost */}
      <ToastContainer
        position="bottom-center"
        className={
          "toastify flex flex-col flex-grow justify-center items-center h-screen w-fit"
        }
        hideProgressBar={true}
        theme="colored"
        newestOnTop
        closeOnClick
        rtl={false}
        autoClose={false}
        pauseOnHover={true}
        pauseOnFocusLoss
        draggable
      />
    </React.StrictMode>
  );
};

export default App;
