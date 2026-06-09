import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";
export default function AccountCategory() {
  const [createview, setCreateView] = useState(true);
  const [alldata, setalldata] = useState("");
  const [alldata1, setalldata1] = useState("");
  const [strGLAccountNumber, setstrGLAccountNumber] = useState("");
  const [strGLDescription, setstrGLDescription] = useState("");
  const [strClosingBalance, setstrClosingBalance] = useState("");
  let AccountNumber = alldata.strGLAccountNumber;
  let Description = alldata.strGLDescription;
  let ClosingBalance = alldata.strClosingBalance;
  let participantID = sessionStorage.getItem("Participantid");
  const closingBalanceHandler = (e) => {
    const inputData = e.target.value;
    setstrClosingBalance(inputData);
    setstrGLDescription(inputData + "" + "Control Account");
  };

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

  useEffect(() => {
    view();
  }, []);
  const view = async () => {
    setCreateView(false);
    try {
      const response = await amsApi.post(
        `gl-account-type-creation/isGLAccountTypeExistDetails`,
        {
          strParticipantId: participantID,
          strGLAccountType: "CTR",
        }
      );
      if (response.data.code === "S0000") {
        setalldata(response.data.isGLAccountNumberExist);
        setCreateView(true);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  // defalut number
  useEffect(() => {
    demo();
  }, {});

  const demo = async () => {
    try {
      const response = await amsApi.post(
        `gl-account-type-creation/accountNumberWithPrefix`,
        {}
      );
      if (response.data.code === "S0000") {
        setalldata1(response.data.accountNoPrefix);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // save form
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      strGLDescription === "" ||
      strGLDescription.length === 0 ||
      strClosingBalance === "" ||
      strGLAccountNumber === ""
    ) {
      setError(true);
    } else if (!participantID) {
      swal("Please check your participantID ");
    } else {
      try {
        const response = await amsApi.post(
          `gl-account-type-creation/addGLAccntType `,
          {
            strGLAccountType: "CTR",
            strParticipantId: participantID,
            strGLDescription: strGLDescription,
            strGLAccountNumber: alldata1 + strGLAccountNumber,
          }
        );
        if (response.data.code === "S0000") {
          setalldata(response.data.isGLAccountNumberExist);
          handleShowSuccess(response.data.message);
          setError("");
          setstrGLDescription("");
          setstrGLAccountNumber("");
          setstrClosingBalance("");
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  return (
    <AppLayout>
      {createview === false && (
        <div className="max-w-7xl mx-auto h-full">
          <div className="w-full shadow-md mt-2 bg-blue-200">
            <div className="px-4 sm:px-10 rounded-t-lg ">
              <div className=" flex  items-center justify-between">
                <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                  Create / View Control Account
                </p>
              </div>
            </div>
          </div>
          <div className=" bg-white sm:rounded-md">
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className="px-4  sm:p-2  bg-white">
                <div className="grid gap-4  md:grid-cols-1 px-8">
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Participant Name{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="participantname"
                      value={strClosingBalance}
                      onChange={closingBalanceHandler}
                      autoComplete="off"
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                    {error && strClosingBalance.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Participant Name!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Account Name <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      value={strGLDescription}
                      onChange={(e) => setstrGLDescription(e.target.value)}
                      id="strGLDescription"
                      autoComplete="off"
                      placeholder="Enter Description"
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                    {error && strGLDescription.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Account Name!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 py-1 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Account Number{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <div className="flex">
                      <input
                        type="text"
                        defaultValue={alldata1}
                        disabled
                        id="strGLAccountNumber"
                        autoComplete="off"
                        placeholder="Enter Account Number"
                        className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      />
                      <input
                        type="text"
                        maxLength={6}
                        minLength={6}
                        Value={strGLAccountNumber}
                        // onChange={(e) => setstrGLAccountNumber(e.target.value)}
                        onChange={(e) => {
                          const value = e.target.value;
                          setstrGLAccountNumber(value);

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
                          if (strGLAccountNumber.length === 0) {
                            setError("Please Enter Account Number!");
                            setErrorMessage("");
                          }
                        }}
                        id="strGLAccountNumber"
                        autoComplete="off"
                        className=" p-2 block w-full shadow-sm sm:text-xs border-gray-300 border rounded-r-md"
                      />
                      {errorMessage ? (
                        <p className="text-red-500 text-xs font-medium">
                          {errorMessage}
                        </p>
                      ) : error && strGLAccountNumber.length <= 0 ? (
                        <p className="text-red-500 text-xs font-medium">
                          Please Enter Account Number!
                        </p>
                      ) : null}
                      {/* {error && strGLAccountNumber.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Enter Account Number!
                        </p>
                      ) : (
                        ""
                      )} */}
                    </div>
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-2 text-right sm:px-6 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  onClick={handleSubmit}
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2.5 px-5  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  // onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2.5 px-5 mx-4 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                >
                  Clear
                </button>
              </div>
            </div>
            {/* </form> */}
          </div>
        </div>
      )}
      {createview === true && (
        <div>
          <div className="min-h-full first-line:flex items-center justify-center ">
            <div className="w-full shadow-md mt-2 bg-blue-200">
              <div className="px-4 sm:px-10 rounded-t-lg ">
                <div className=" flex  items-center justify-between">
                  <p className="text-sm  sm:text-xm text-black :text-2xl  leading-normal ">
                    Create / View Control Account
                  </p>
                </div>
              </div>
            </div>
            <div className="bg-white sm:rounded-md">
              <form className="">
                <div className="overflow-hidden shadow sm:rounded-md">
                  <div className=" px-4  sm:p-2 bg-white">
                    <div className="grid gap-4 md:grid-cols-1">
                      <div className="flex">
                        <label
                          htmlFor="text"
                          className="block w-auto px-1 py-1 text-sm font-normal text-gray-700 bg-white  "
                        >
                          Account Number{""}
                          <span className="text-red-600 px-2">*</span>
                        </label>
                        <input
                          type="text"
                          id="AccounNumber"
                          disabled
                          className="block w-60 px-3 py-1 text-sm  text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Account Number"
                          value={AccountNumber}
                        />
                      </div>
                      <div className="flex">
                        <label
                          htmlFor="text"
                          className="block w-auto px-3 py-1 text-sm font-normal text-gray-700 bg-white  "
                        >
                          Account Name{""}
                          <span className="text-red-600 px-2">*</span>
                        </label>
                        <input
                          type="text"
                          id=" Account Name"
                          value={Description}
                          disabled
                          className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Description"
                        />
                      </div>
                      <div className="flex">
                        <label
                          htmlFor="text"
                          className="block w-auto px-2 py-1 text-sm font-normal text-gray-700 bg-white  "
                        >
                          Account Balance
                          <span className="text-red-600 px-2">*</span>
                        </label>
                        <input
                          type="text"
                          id="Participant Name"
                          value={ClosingBalance}
                          disabled
                          className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Closing Balance "
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
