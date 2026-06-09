import React, { useState, Fragment } from "react";
import AppLayout from "../layout/AppLayout";
import { XMarkIcon } from "@heroicons/react/24/solid";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import { Dialog, Transition } from "@headlessui/react";
import amsApi from "../api/amsApi";
import { useNavigate } from "react-router-dom";
import CustomAlert from "../layout/CustomAlert";

export default function Daccountnumber() {
  const [accountno, setAccountno] = useState("");

  const [selectedOption, setSelectedOption] = useState("");
  console.log(selectedOption);
  const handleOptionChange = (event) => {
    setSelectedOption(event.target.value);
  };

  const [errorMessage, setErrorMessage] = useState("");
  const [error, setError] = useState("");
  const [alldata, setAlldata] = useState({});
  // let name = data.accountHolderName;
  // let accountType = data.accountType;
  // let creationDate = data.creationDate;
  // let lastTxnDate = data.lastTxnDate;
  // let closingBalance = data.closingBalance;
  // let dormantMarkedDate = data.dormantMarkedDate;
  // let status = data.status;
  // let Accno = data.accountNumber;

  let [isOpen, setIsOpen] = useState(false);

  function closeModal() {
    setIsOpen(false);
  }

  function openModal() {
    setIsOpen(true);
  }

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

  const Accountno = async () => {
    if (accountno === "" || accountno.length === 0) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `dormancy/makervalidation`,
          {
            strAccountNumber: accountno,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          setAlldata(response.data.dormantAccountInformation);
          // navigate("/dormanttable"); // navigate to new page
          navigate("/dormanttable", {
            state: { alldata: response.data.dormantAccountInformation },
          });
          // handleShowSuccess(response.data.message);
          // openModal();
          setError("");
          Clear();
        } else {
          handleShowError(response.data.message);
          // openModal();
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  const Clear = () => {
    setAccountno("");
    setError("");
    setErrorMessage("");
  };

  // Create Dormant form
  const navigate = useNavigate();

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2">
          <div className="px-4 sm:px-10  bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal  ">
                Create Dormancy
              </p>
            </div>
          </div>
        </div>

        <div className=" md:col-span-2 lg:col-span-1  ">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-4 py-1 sm:p-2 bg-white ">
                <div className="grid gap-6  md:grid-cols-3 px-8">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700 dark:text-white"
                    >
                      Account Number{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      title="Account number"
                      type="text"
                      id="text"
                      value={accountno}
                      // onChange={handleAccountnoChange}
                      onChange={(e) => {
                        const value = e.target.value;
                        setAccountno(value);

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
                        if (accountno.length === 0) {
                          setError("Please Enter Account number!");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      placeholder="Enter Account Number"
                      autoComplete="off"
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage}
                      </p>
                    ) : error && accountno.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Account number!
                      </p>
                    ) : null}
                  </div>

                  <div className="">
                    <button
                      title="Click Search Button"
                      type="button"
                      onClick={Accountno}
                      data-modal-toggle="defaultModal"
                      className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white font-bold py-2 px-4 border border-blue-700 rounded"
                    >
                      <MagnifyingGlassIcon className="h-3 w-3" />
                    </button>
                    <button
                      title="Clear Data"
                      type="button"
                      onClick={Clear}
                      className="inline-flex justify-center bg-rose-500 hover:bg-rose-700 focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 py-2 px-4 mx-2 my-5 font-bold  text-white  border border-rose-700 rounded"
                    >
                      <XMarkIcon className="h-3 w-3" />
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
    </AppLayout>
  );
}
