import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import swal from "sweetalert";
import Swal from "sweetalert2";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import { XMarkIcon } from "@heroicons/react/24/solid";
import CustomAlert from "../../layout/CustomAlert";
export default function TransferInOut() {
  const [showhide, setShowhide] = useState("");
  const [amount, setAmount] = useState("");
  const [data, setData] = useState({});
  const [transferIn, settransferIn] = useState("");
  let accountType = data.strIsThirdPartyTransfer;
  let participantID = sessionStorage.getItem("Participantid");
  const [error, setError] = useState("");
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

  // UI in and out field
  const handlesshowhide = (event) => {
    const getuser = event.target.value;
    setShowhide(getuser);
    settransferIn(event.target.value);
  };

  //
  useEffect(() => {
    TxnInOut();
  }, []);

  const TxnInOut = async () => {
    try {
      const response = await amsApi.post(
        `gl-account-type-creation/glCtrAccountTypObj`,
        {
          strGLAccountType: "CTR",
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setData(response.data.glAccountCreation);
        // showSuccess(response.data.message);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  // Transfer IN/Out submit
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (amount === "" || amount.length === 0) {
      swal("Please Enter empty input field");
    } else if (!participantID) {
      swal("Please check your participantID ");
    } else {
      try {
        const response = await amsApi.post(
          `gl-account-type-creation/updateGLClosingBalanc`,
          {
            strClosingBalance: amount,
            strIsThirdPartyTransfer: transferIn,
            strParticipantId: participantID,
          }
        );
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
        } else {
          handleShowSuccess(response.data.message);
        }
      } catch (error) {
        handleShowSuccess(error.response.data.message);
      }
    }
  };
  const handleClick = () => {
    setAmount("");
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Transfer IN / OUT
              </p>
            </div>
          </div>
        </div>

        <div className="md:col-span-2 lg:col-span-1 ">
          <form className="" onSubmit={handleSubmit}>
            <div className="overflow-hidden shadow ">
              <div className=" px-4 py-2 sm:p-2 bg-white ">
                <div className="grid gap-3 grid-cols-3 px-8 ">
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Select Transfer Option
                    </label>
                    <select
                      id="transferIn"
                      name="transferIn"
                      onChange={(e) => handlesshowhide(e)}
                      value={transferIn}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      <option value="In">Transfer In</option>
                      <option value="Out">Transfer Out</option>
                    </select>
                  </div>
                  {showhide === "In" && (
                    <div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Enter Amount to perform Transfer In
                        </label>
                        <input
                          type="text"
                          id="amount"
                          name="amount"
                          value={amount}
                          // onChange={(e) => setAmount(e.target.value)}
                          onChange={(e) => {
                            const value = e.target.value;
                            setAmount(value);

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
                            if (amount.length === 0) {
                              setError("Please enter Amount!");
                              setErrorMessage("");
                            }
                          }}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Amount"
                        />
                        {errorMessage ? (
                          <p className="text-red-500 text-xs font-medium">
                            {errorMessage}
                          </p>
                        ) : error && amount.length <= 0 ? (
                          <p className="text-red-500 text-xs font-medium">
                            Please enter Amount!
                          </p>
                        ) : null}
                      </div>
                    </div>
                  )}
                  {showhide === "Out" && (
                    <div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Enter Amount to perform Transfer Out
                        </label>
                        <input
                          type="text"
                          id="amount"
                          name="amount"
                          value={amount}
                          // onChange={(e) => setAmount(e.target.value)}
                          onChange={(e) => {
                            const value = e.target.value;
                            setAmount(value);

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
                            if (amount.length === 0) {
                              setError("Please enter Amount!");
                              setErrorMessage("");
                            }
                          }}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Amount"
                        />
                        {errorMessage ? (
                          <p className="text-red-500 text-xs font-medium">
                            {errorMessage}
                          </p>
                        ) : error && amount.length <= 0 ? (
                          <p className="text-red-500 text-xs font-medium">
                            Please enter Amount!
                          </p>
                        ) : null}
                      </div>
                    </div>
                  )}
                  <div className="my-4">
                    <button
                      title="Click Search Button"
                      type="submit"
                      data-modal-toggle="defaultModal"
                      className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800   focus:ring-offset-2 text-white font-md py-2 px-4 border border-blue-700 rounded  shadow-lg inner"
                    >
                      <MagnifyingGlassIcon className="h-3 w-3" />
                    </button>
                    <button
                      title="Clear Data"
                      type="button"
                      onClick={handleClick}
                      className="inline-flex justify-center bg-rose-500 hover:bg-rose-700 focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 py-2 px-4 mx-2  font-md  text-white  border  shadow-lg inner border-rose-700 rounded"
                    >
                      <XMarkIcon className="w-3 h-3" />
                    </button>
                  </div>
                </div>
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
