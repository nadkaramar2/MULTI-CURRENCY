import React, { useState } from "react";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { Bars3Icon, XMarkIcon } from "@heroicons/react/24/outline";
import { Fragment } from "react";
import { Transition, Menu, Disclosure } from "@headlessui/react";
import { logout } from "../store/authFeatures/authSlice";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";

import amsApi from "../api/amsApi";

// import montra from "../assets/img/montra.png";
import TranEco from "../assets/img/TranEco.png";
import CustomAlert from "../layout/CustomAlert";
export default function Navbar() {
  const { authInfo } = useSelector((state) => state.auth);
  const LastLoginDate = authInfo.lastLoginDate;

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };
  function reloadPage() {
    setTimeout(function () {
      // eslint-disable-next-line no-restricted-globals
      window.location.reload();
    }, 1000);
  }

  const UserId = authInfo.userId;

  const Role = authInfo.roleId;

  const [, setCollapseShow] = useState("hidden");

  const key = new Date().getMilliseconds();

  const navigate = useNavigate();

  const dispatch = useDispatch();

  // signout handler
  let sms = "Logout successful";
  const SignoutHandler = async () => {
    // window.location.reload();
    // navigate("/ams/login", { replace: true });
    const p = sessionStorage.getItem("Participantid");
    try {
      for (var key in sessionStorage) {
        if (key === "Participantid" && sessionStorage.getItem(key) === p) {
          sessionStorage.removeItem(key);
          break; // Exit the loop once the item is removed
        }
      }
      const storedToken = localStorage.getItem("token");
      const response = await amsApi.post(
        `/signnOut`,

        {
          userName: UserId,
          // strUserId: UserId,
        },

        {
          headers: {
            "Content-Type": "application/json",

            Authorization: `Bearer ${storedToken}`,
          },
        }
      );

      if (response.status === 200) {
        await handleShowSuccess(sms);
        console.log("Logout Successfull");
        reloadPage();
      } else {
        await handleShowSuccess(sms);
        reloadPage();
      }
    } catch (error) {
      await handleShowSuccess(sms);
      reloadPage();
    }
    await handleShowSuccess(sms);
  };

  const Signout = () => {
    dispatch(logout());
    SignoutHandler();
    navigate("/ams/login", { replace: true });
  };
  // useEffect(() => {
  //   const handleBeforeUnload = (event) => {
  //     const customMessage = "Are you sure you want to leave?";
  //     alert(customMessage);
  //     event.returnValue = customMessage; // For older browsers
  //     return customMessage; // For modern browsers
  //   };

  //   window.addEventListener("beforeunload", handleBeforeUnload);

  //   return () => {
  //     window.removeEventListener("beforeunload", handleBeforeUnload);
  //   };
  // }, []);

  // window.addEventListener("beforeunload", function () {
  //   const p = sessionStorage.getItem("Participantid");
  //   for (var key in sessionStorage) {
  //     if (key === "Participantid" && sessionStorage.getItem(key) === p) {
  //       sessionStorage.removeItem(key);
  //       break; // Exit the loop once the item is removed
  //     }
  //   }
  //   navigate("/ams/login", { replace: true });
  // });

  return (
    <>
      <Disclosure
        as="nav"
        key={key}
        style={{ backgroundColor: "#FFFFFF" }}
        className="sticky top-0 z-20"
      >
        {({ open }) => (
          <>
            <div
              key={key}
              className="w-full mx-auto px-4  relative sm:px-6 lg:px-8 border-2 border-indigo-200"
            >
              <div className="flex items-center justify-between h-12 ">
                <div className="flex-shrink-0">
                  <Link
                    exact={true.toString()}
                    to="dashboard"
                    className=" flex items-center text-lg  lg:text-xl xl:text-4xl  text-gray-100 font-bold"
                  >
                    <img
                      className="font-semibold rounded-lg w-32 flex items-center justify-center"
                      src={TranEco}
                    />

                    {""}

                    {/* <span className="mx-2">AMS</span> */}
                  </Link>
                </div>

                <div className="hidden lg:block">
                  <div className="ml-10 flex items-center justify-center space-x-4 bg-white rounded-lg ">
                    <div className="items-end">
                      <span className="hidden text-md sm:block font-normal mx-4 ">
                        Last Login : {LastLoginDate}
                      </span>
                    </div>

                    {/* {authInfo && ( */}

                    <Menu as="div" className=" relative">
                      <Menu.Button className="max-w-xs text-black rounded-full flex items-center text-sm">
                        <span className=" font-semibold text-black rounded-full  h-10 w-10 flex items-center  focus:ring-gray-800 focus:ring-2  focus:ring-offset-2 justify-center">
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            className="h-15 w-15"
                            viewBox="0 0 20 20"
                            fill="#fffff"
                          >
                            <path
                              fillRule="evenodd"
                              d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-6-3a2 2 0 11-4 0 2 2 0 014 0zm-2 4a5 5 0 00-4.546 2.916A5.986 5.986 0 0010 16a5.986 5.986 0 004.546-2.084A5 5 0 0010 11z"
                              clipRule="evenodd"
                            />
                          </svg>
                        </span>
                      </Menu.Button>

                      <Transition
                        as={Fragment}
                        enter="transition ease-out duration-100"
                        enterFrom="transform opacity-0 scale-95"
                        enterTo="transform opacity-100 scale-100"
                        leave="transition ease-in duration-75"
                        leaveFrom="transform opacity-100 scale-100"
                        leaveTo="transform opacity-0 scale-95"
                      >
                        <Menu.Items className="origin-top-right absolute   right-0 mt-4 w-64 rounded-md shadow-lg py-1 bg-white ring-1 ring-black ring-opacity-5 focus:outline-none">
                          <Menu.Item>
                            <p className="flex px-4  text-lg mx-2 text-left my-2 justify-start items-center  rounded-md  font-medium focus:outline-none focus:ring-2  focus:ring-opacity-50">
                              {UserId}
                            </p>
                          </Menu.Item>

                          <Menu.Item>
                            <p className="flex px-4 text-lg mx-2 text-left my-2 justify-start items-center  rounded-md  font-medium focus:outline-none focus:ring-2  focus:ring-opacity-50">
                              {Role}
                            </p>
                          </Menu.Item>

                          <Menu.Item>
                            <p className="flex px-4 text-lg mx-2 text-left my-2 justify-start items-center  rounded-md  font-medium focus:outline-none focus:ring-2  focus:ring-opacity-50">
                              {LastLoginDate}
                            </p>
                          </Menu.Item>

                          <Menu.Item>
                            <p
                              onClick={Signout}
                              className="flex px-4 py-2 text-lg mx-2 my-2 justify-center items-center bg-red-400 rounded-md text-center font-bold  focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-opacity-50  text-gray-50 cursor-pointer "
                            >
                              Sign out
                            </p>
                          </Menu.Item>
                        </Menu.Items>
                      </Transition>
                    </Menu>

                    {/* )} */}
                  </div>
                </div>

                <div className=" flex lg:hidden">
                  {/* Mobile menu button  */}

                  <Disclosure.Button className=" inline-flex items-center justify-center  ">
                    <span className="sr-only">Open main menu</span>

                    {open ? (
                      <XMarkIcon
                        onClick={() => setCollapseShow("hidden")}
                        className="block h-7 w-7 rounded text-red-500 hover:text-white hover:bg-red-500 outline-none ring-2  ring-red-500"
                        aria-hidden="true"
                      />
                    ) : (
                      <Bars3Icon
                        onClick={() =>
                          setCollapseShow("bg-blue-200 m-2 py-3 px-6")
                        }
                        className="block h-8 w-8 m-1  rounded text-gray-900 hover:text-white hover:bg-gray-700 "
                        aria-hidden="true"
                      />
                    )}
                  </Disclosure.Button>
                </div>
              </div>
            </div>

            <Disclosure.Panel className="lg:hidden py-4 mx-5">
              <div className="md:px-8 flex flex-col justify-center  items-Start space-y-2 ">
                {authInfo && (
                  <>
                    <div className="border-t-2 border-white"></div>

                    <div className="flex-col items-center py-4 justify-between space-y-4">
                      <div className="flex-shrink-0 flex  items-center">
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          className="h-10 w-10"
                          viewBox="0 0 20 20"
                          fill="currentColor"
                        >
                          <path
                            fillRule="evenodd"
                            d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-6-3a2 2 0 11-4 0 2 2 0 014 0zm-2 4a5 5 0 00-4.546 2.916A5.986 5.986 0 0010 16a5.986 5.986 0 004.546-2.084A5 5 0 0010 11z"
                            clipRule="evenodd"
                          />
                        </svg>

                        <p className="flex px-4 py-2 text-lg mx-2 text-left my-2 justify-start items-center  rounded-md  font-medium focus:outline-none focus:ring-2  focus:ring-opacity-50  text-gray-50">
                          {Role}
                        </p>
                      </div>
                    </div>

                    <button
                      title="Signout Here"
                      onClick={Signout}
                      type="button"
                      className=" items-center  py-2 px-4 my-4  text-center border-transparent rounded-md  text-lg font-medium text-white bg-red-600  focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 cursor-pointer "
                    >
                      Signout
                    </button>
                  </>
                )}
              </div>
            </Disclosure.Panel>
          </>
        )}
      </Disclosure>

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </>
  );
}
