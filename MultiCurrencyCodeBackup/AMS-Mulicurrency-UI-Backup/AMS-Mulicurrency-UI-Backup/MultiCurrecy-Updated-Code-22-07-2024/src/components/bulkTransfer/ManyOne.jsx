import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import swal from "sweetalert";
import Swal from "sweetalert2";
import { Tab, Tabs, TabList, TabPanel } from "react-tabs";
import "react-tabs/style/react-tabs.css";
import CustomAlert from "../../layout/CustomAlert";
export default function ManyOne() {
  const [type, setType] = useState([]);
  const [accounttype, setAccountType] = useState("");
  const [accountno, setAccountno] = useState("");
  const [amount, setAmount] = useState("");
  const [totype, setToType] = useState([]);
  const [data, setData] = useState([]);
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [errorMessage1, setErrorMessage1] = useState("");
  let useid = localStorage.getItem("userName");

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

  const [isValidateButtonDisabled, setIsValidateButtonDisabled] =
    useState(false);
  const [isRowAdded, setIsRowAdded] = useState(true);
  const [inputFields, setInputFields] = useState([
    {
      strFromAccountType: "",
      strFromAccountNo: "",
      strTransactionAmount: "",
      holdername: "",
    },
  ]);

  // start
  const validateTransferAmount = (mainTransferAmount, inputFields) => {
    for (const field of inputFields) {
      const transferAmount = parseFloat(field.strTransactionAmount);
      if (transferAmount > mainTransferAmount) {
        swal(
          "Validation Error",
          "Transfer amount cannot be greater than the main transfer amount.",
          "error"
        );
        return false;
      }
    }
    return true;
  };

  const calculateTotalTransferAmount = (inputFields) => {
    let totalTransferAmount = 0;
    for (const field of inputFields) {
      totalTransferAmount += parseFloat(field.strTransactionAmount);
    }
    return totalTransferAmount;
  };

  const handleAddFields = () => {
    const lastRow = inputFields[inputFields.length - 1];
    const mainTransferAmount = parseFloat(amount);

    // Check if required fields for the "From Account Information" are empty
    if (accounttype === "" || accountno === "" || mainTransferAmount <= 0) {
      swal(
        "Validation Error",
        "Please fill in all required fields (Account Type, Account Number, and Amount) in From Account Information.",
        "error"
      );
      return;
    }

    // Check if any of the "To Account Information" fields are empty in any row
    for (const field of inputFields) {
      if (
        field.strFromAccountType === "" ||
        field.strFromAccountNo === "" ||
        field.strTransactionAmount <= 0
      ) {
        swal(
          "Validation Error",
          "Please fill in all required fields (Account Type, Account Number, and Amount) in To Account Information.",
          "error"
        );
        return;
      }
    }

    if (!validateTransferAmount(mainTransferAmount, inputFields)) {
      return;
    }

    const totalTransferAmount = calculateTotalTransferAmount(inputFields);

    if (totalTransferAmount >= mainTransferAmount) {
      swal(
        "Validation Error",
        "Total transfer amount reached. Cannot add more rows.",
        "error"
      );
      setIsValidateButtonDisabled(true);
      return;
    }

    const newInputFields = [
      ...inputFields,
      {
        strFromAccountType: "",
        strFromAccountNo: "",
        strTransactionAmount: "", // User will input this manually
        holdername: "",
        isRowValid: false, // Add a new property to track validation status
      },
    ];

    // Set the validation status of the previous row to true
    if (inputFields.length > 0) {
      const prevRowIdx = inputFields.length - 1;
      newInputFields[prevRowIdx].isRowValid = true;
    }

    setInputFields(newInputFields);
    setShowOldButton(true);
  };

  const handleInputChange = async (index, event) => {
    const values = [...inputFields];
    values[index][event.target.name] = event.target.value;
    // Fetch account holder name based on account type and account number
    const response = await amsApi.post(
      `account/getAccountBalancAndName`,
      {
        strAccountType: values[index].strFromAccountType,
        strAccountNumber: values[index].strFromAccountNo,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    if (response.data.code === "S0000") {
      values[index].holdername =
        response.data.accountInfoList[0].strAccountHolderName;
    } else {
      values[index].holdername = ""; // Clear holder name if fetching fails
    }
    setInputFields(values);
  };
  // replace button
  const [showOldButton, setShowOldButton] = useState(true);
  const handleOldButtonClick = () => {
    setIsRowAdded(false);
  };
  // end
  useEffect(() => {
    Accounttype();
  }, []);

  const Accounttype = async () => {
    try {
      const response = await amsApi.get(
        `accountType/getAccntType`,

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setType(response.data.accountTypeMasterlistData);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);

      // handleShowError(
      //   "Sorry, the server is under mentaines. Please try again later"
      // );
    }
  };

  // To account type
  useEffect(() => {
    toAccounttype();
  }, []);

  const toAccounttype = async () => {
    try {
      const response = await amsApi.get(
        `accountType/getAccntType`,

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setToType(response.data.accountTypeMasterlistData);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      handleShowError(error);
    }
  };
  const HolderName = data.map((t) => [t.strAccountHolderName]);
  sessionStorage.setItem("holderName", HolderName);
  let holderName = sessionStorage.getItem("holderName");

  const ClosingBalance = data.map((t) => [t.strClosingBalance]);
  sessionStorage.setItem("closingBalance", ClosingBalance);
  let Balance = sessionStorage.getItem("closingBalance");
  const BalancAndName = async () => {
    try {
      const response = await amsApi.post(
        `account/getAccountBalancAndName`,
        {
          strAccountType: accounttype,
          strAccountNumber: accountno,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setData(response.data.accountInfoList);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      handleShowError(error);
    }
  };

  const ManytoOne = async () => {
    if (
      accounttype === "" ||
      accounttype.length === 0 ||
      accountno === "" ||
      amount === ""
    ) {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `bulkTransfer/manyToOne`,
          {
            bulkTransferFromAccounts: inputFields,
            strToAccountNo: accountno,
            strToAccountType: accounttype,
            strTransferAmount: amount,
            strMakerId: useid,
            strIsVerified: "Y",
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          clearFields();
        } else {
          handleShowError(response.data.message);
          clearFields();
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  const clearFields = () => {
    setAccountType("");
    setAccountno("");
    setAmount("");
    setData([]);
    setInputFields([
      {
        strFromAccountType: "",
        strFromAccountNo: "",
        strTransactionAmount: "",
        holdername: "",
      },
    ]);
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Many To One
              </p>
            </div>
          </div>
        </div>
        <div className="overflow-hidden shadow sm:rounded-md mt-2">
          <div className="grid  md:grid-cols-1 ">
            <div className=" sm:p-2 bg-blue-100 ">
              <h1 className="font-medium flex justify-center  text-black text-2xl mb-4">
                To Account Information
              </h1>
              <div className="grid gap-3 mb-1 md:grid-cols-3 lg:grid-cols-5 px-8 ">
                <div>
                  <label
                    htmlFor="GL Account Type"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Account Type <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    name="type"
                    id="type"
                    value={accounttype}
                    onChange={(e) => setAccountType(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="select"
                  >
                    <option value="">Select</option>
                    {type.map((data) => (
                      <optgroup key={uuidv4()}>
                        <option value={data.strAccountType}>
                          {data.strAccountType || ""} -
                          {data.strDescription || ""}
                        </option>
                      </optgroup>
                    ))}
                  </select>
                  {error && accounttype.length <= 0 ? (
                    <p className="text-red-500   text-xs font-medium">
                      Please Select Account type !
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div className="">
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Account Number
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="accountno"
                    value={accountno}
                    autoComplete="off"
                    // onChange={(e) => setAccountno(e.target.value)}
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
                    onKeyUp={BalancAndName}
                    onBlur={() => {
                      if (accountno.length === 0) {
                        setError(" Please Enter Account number!");
                        setErrorMessage("");
                      }
                    }}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter Account number"
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
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    A/C Holder Name
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    // onClick={BalancAndName}
                    defaultValue={holderName}
                    autoComplete="off"
                    disabled
                    placeholder="Enter Holder name"
                    // disabled
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  />
                </div>
                <div className="">
                  {""}
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Available Balance
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    disabled
                    defaultValue={Balance}
                    placeholder="Enter Available Balance "
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  />
                </div>
                <div className="">
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Transfer Amount
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="amount"
                    value={amount}
                    // onChange={(e) => setAmount(e.target.value)}
                    onChange={(e) => {
                      const value = e.target.value;
                      setAmount(value);

                      const regex = /^[0-9]*$/;
                      if (!regex.test(value)) {
                        setErrorMessage1(
                          " Only allow numeric digits"
                          // "Please enter only numeric values"
                        );
                      } else {
                        setErrorMessage1("");
                      }
                      setError("");
                    }}
                    onBlur={() => {
                      if (amount.length === 0) {
                        setError("Please Enter amount!");
                        setErrorMessage1("");
                      }
                    }}
                    autoComplete="off"
                    placeholder="Enter Amount to Transfer"
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  />
                  {errorMessage1 ? (
                    <p className="text-red-500 text-xs font-medium">
                      {errorMessage1}
                    </p>
                  ) : error && amount.length <= 0 ? (
                    <p className="text-red-500 text-xs font-medium">
                      Please Enter amount!
                    </p>
                  ) : null}
                  {/* {error && amount.length <= 0 ? (
                  <p className="text-red-500   text-xs font-medium">
                    Please Enter amount !
                  </p>
                ) : (
                  ""
                )} */}
                </div>
              </div>
            </div>
          </div>

          {/* from Account Form */}

          <div className="overflow-x-auto relative shadow-md  bg-white">
            <h1 className="font-medium flex justify-center  text-black text-2xl ">
              From Account Information
            </h1>
            {inputFields.map((inputField, index) => (
              <div
                key={index}
                className="grid gap-3 mb-1 md:grid-cols-5 px-8 mt-3 "
              >
                <div>
                  <label
                    htmlFor="GL Account Type"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Account Type <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    type="text"
                    placeholder="strFromAccountType"
                    name="strFromAccountType"
                    value={inputField.strFromAccountType}
                    onChange={(event) => handleInputChange(index, event)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value="">Select</option>
                    {totype.map((data) => (
                      <optgroup key={uuidv4()}>
                        <option value={data.strAccountType}>
                          {data.strAccountType || ""} -
                          {data.strDescription || ""}
                        </option>
                      </optgroup>
                    ))}
                  </select>
                </div>
                <div className="">
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Account number
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="strFromAccountNo"
                    name="strFromAccountNo"
                    value={inputField.strFromAccountNo}
                    autoComplete="off"
                    onChange={(event) => handleInputChange(index, event)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter Account Number"
                  />
                </div>

                <div className="">
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Account Holder name
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="holdername"
                    name="holdername"
                    value={inputField.holdername}
                    autoComplete="off"
                    disabled
                    placeholder="EnterAccount Holder name "
                    onChange={(event) => handleInputChange(index, event)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  />
                </div>
                <div className="">
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Amount To Transfer
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="strTransactionAmount"
                    name="strTransactionAmount"
                    value={inputField.strTransactionAmount}
                    onChange={(event) => handleInputChange(index, event)}
                    autoComplete="off"
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter Amount To Transfer"
                  />
                </div>

                <div className="">
                  <label
                    htmlFor="text"
                    className="flex justify-start  text-xs font-medium text-black"
                  >
                    Action
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  {!isRowAdded || index !== inputFields.length - 1 ? (
                    <button
                      title="Add row Here"
                      onClick={handleOldButtonClick}
                      disabled={isRowAdded}
                      className="bg-blue-700  cursor-not-allowed block w-auto px-3 py-1 text-sm text-white bg-clip-padding  border-solid border-gray-500 rounded transition ease-in-out m-0 "
                    >
                      Add Row{""}
                    </button>
                  ) : (
                    <button
                      title="Validate Data"
                      onClick={handleAddFields}
                      disabled={isValidateButtonDisabled}
                      className={`bg-red-700 block w-auto px-3  py-1 text-sm text-white bg-clip-padding border-solid border-gray-500 rounded transition ease-in-out m-0 ${
                        isValidateButtonDisabled ? "cursor-not-allowed" : ""
                      }`}
                    >
                      Validate
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>
        <div className="bg-gray-100 px-4 py-2 text-right sm:px-2 ">
          <button
            title="Click Submit Button"
            type="button"
            onClick={ManytoOne}
            data-modal-toggle="defaultModal"
            className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
          >
            Submit
          </button>
          <button
            title="Clear Data"
            type="submit"
            data-modal-toggle="defaultModal"
            onClick={clearFields}
            className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
          >
            Clear
          </button>
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
