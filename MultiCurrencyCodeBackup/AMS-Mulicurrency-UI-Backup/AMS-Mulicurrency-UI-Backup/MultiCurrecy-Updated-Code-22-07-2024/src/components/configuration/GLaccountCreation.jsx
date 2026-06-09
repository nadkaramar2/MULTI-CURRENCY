import React, { useState } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";
export default function GLaccountCreation() {
  const [GLAccountNumber, setGLAccountNumber] = useState("");
  const [GLAccountType, setGLAccountType] = useState("");
  const [GLDescription, setGLDescription] = useState("");
  const [ThirdPartyAllow, setThirdPartyAllow] = useState("");
  // const [isMulticurrency, setisMulticurrency] = useState("");
  const [GLAccountNumber1, setGLAccountNumber1] = useState("");
  const [error, setError] = useState("");
  let participantID = sessionStorage.getItem("Participantid");
  const [errorMessage, setErrorMessage] = useState("");
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
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      GLAccountNumber === "" ||
      GLAccountNumber.length === 0 ||
      GLAccountType === "" ||
      GLAccountType.length === 0 ||
      GLDescription === "" ||
      GLDescription.length === 0 ||
      ThirdPartyAllow === "" ||
      ThirdPartyAllow.length === 0
    ) {
      setError(true);
    } else if (!participantID) {
      swal("Please check your participantID ");
    } else {
      try {
        const response = await amsApi.post(
          `gl-account-type-creation/addGLAccntType`,
          {
            strGLAccountType: GLAccountType,
            strGLDescription: GLDescription,
            strGLAccountNumber: 999 + GLAccountNumber1 + GLAccountNumber,
            strThirdPartyAllow: ThirdPartyAllow,
            // isMulticurrency: isMulticurrency,
            strParticipantId: participantID,
          }
        );

        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          handleClick();
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  const handleClick = () => {
    setGLAccountNumber("");
    setGLAccountType("");
    setGLDescription("");
    setThirdPartyAllow("");
    setGLAccountNumber1("");
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 py-1 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base sm:text-xm text-black :text-2xm  leading-normal ">
                GL Account Creation
              </p>
            </div>
          </div>
        </div>
        <div className="bg-white sm:rounded-md">
          <form action="#" method="POST" onSubmit={handleSubmit}>
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className="px-4  sm:p-2 bg-white  ">
                <div className="grid gap-2  lg:grid-cols-1 md:grid-cols-1 px-2">
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 ml-1 mr-14 text-sm font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      GL Account Type
                      <span className="text-red-600  px-3 ">*</span>
                    </label>
                    <input
                      type="text"
                      value={GLAccountType}
                      onChange={(e) =>
                        setGLAccountType(e.target.value.toUpperCase())
                      }
                      autoComplete="off"
                      placeholder="Enter GLAccount Type"
                      className="block w-60 px-3 py-1 sm:text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      // className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out"
                    />
                    {error && GLAccountType.length <= 0 ? (
                      <p className="text-red-500 text-sm font-medium">
                        Please Enter Account Type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 mr-9 text-sm font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      GL Account Description{""}
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <input
                      type="text"
                      value={GLDescription}
                      disabled={!GLAccountType}
                      onChange={(e) => setGLDescription(e.target.value)}
                      autoComplete="off"
                      placeholder=" Enter GL Description"
                      className="block w-60 px-3 py-1  sm:text-sm  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      // className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                    {error && GLDescription.length <= 0 ? (
                      <p className="text-red-500   text-sm font-medium">
                        Please enter Account Description
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1  mr-14 text-sm font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      GL Account Number{""}
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <div className="flex">
                      <input
                        type="text"
                        value={999}
                        id="GLAccountNumber"
                        autoComplete="off"
                        disabled
                        className="block w-12 px-3 py-1 text-sm  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded-l-md transition ease-in-out m-0"
                      />

                      <input
                        type="text"
                        maxLength={2}
                        minLength={2}
                        // onChange={(e) => setGLAccountNumber1(e.target.value)}
                        id="AccountNumber1"
                        autoComplete="off"
                        className=" p-2 block w-10 shadow-sm sm:text-xs border border-gray-500 "
                        placeholder="00"
                        value={GLAccountNumber1}
                        disabled={!GLDescription}
                        onChange={(e) => {
                          const value = e.target.value;
                          setGLAccountNumber1(value);

                          const regex = /^[0-9]*$/;
                          if (!regex.test(value)) {
                            setErrorMessage(
                              " Only allow numeric digits"
                              // "Please enter only numeric values"
                            );
                          } else {
                            setErrorMessage("");
                          }
                          setError("");
                        }}
                        onBlur={() => {
                          if (GLAccountNumber1.length === 0) {
                            setError("Please Enter GL Account number");
                            setErrorMessage("");
                          }
                        }}
                      />
                      <input
                        type="text"
                        maxLength={7}
                        minLength={7}
                        value={GLAccountNumber}
                        disabled={!GLDescription}
                        // onChange={(e) => setGLAccountNumber(e.target.value)}
                        onChange={(e) => {
                          const value = e.target.value;
                          setGLAccountNumber(value);

                          const regex = /^[0-9]*$/;
                          if (!regex.test(value)) {
                            setErrorMessage("Please enter only numeric values");
                          } else {
                            setErrorMessage("");
                          }
                          setError("");
                        }}
                        onBlur={() => {
                          if (GLAccountNumber.length === 0) {
                            setError("Please Enter GL Account number");
                            setErrorMessage("");
                          }
                        }}
                        autoComplete="off"
                        placeholder="Enter GLAccount number "
                        className=" p-2 block w-full shadow-sm sm:text-xs border border-gray-500 rounded-r-md"
                      />
                    </div>
                    {errorMessage ? (
                      <p className="text-red-500 text-sm font-medium">
                        {errorMessage}
                      </p>
                    ) : error && GLAccountNumber.length <= 0 ? (
                      <p className="text-red-500 text-sm font-medium">
                        Please Enter GL Account number
                      </p>
                    ) : null}
                    {/* {error && GLAccountNumber.length <= 0 ? (
                          <p className="text-red-500   text-sm font-medium">
                            Please enter Account Number{""}
                            <span className="text-red-600">*</span>
                          </p>
                        ) : (
                          ""
                        )} */}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-1   py-1 text-sm font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      Third Party Transaction Allow{""}
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <select
                      id="accountType"
                      name="accountType"
                      value={ThirdPartyAllow}
                      disabled={!GLAccountNumber}
                      onChange={(e) => setThirdPartyAllow(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select </option>
                      <option value="Y">Yes</option>
                      <option value="N">No</option>
                    </select>
                    {error && ThirdPartyAllow.length <= 0 ? (
                      <p className="text-red-500   text-sm font-medium">
                        Please enter select Third Party Allow
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  {/* <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-14   py-1 text-sm font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      Multi Currency Allow
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <select
                      id="isMulticurrency"
                      name="isMulticurrency"
                      value={isMulticurrency}
                      disabled={!ThirdPartyAllow}
                      onChange={(e) => setisMulticurrency(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select </option>
                      <option value="Y">Yes</option>
                      <option value="N">No</option>
                    </select>
                    {error && isMulticurrency.length <= 0 ? (
                      <p className="text-red-500   text-sm font-medium">
                        Please Enter Select MultiCurrency
                      </p>
                    ) : (
                      ""
                    )}
                  </div> */}
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-1 text-right sm:px-2 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-4  text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Search Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-4 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                >
                  Clear
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* </div> */}
    </AppLayout>
  );
}
