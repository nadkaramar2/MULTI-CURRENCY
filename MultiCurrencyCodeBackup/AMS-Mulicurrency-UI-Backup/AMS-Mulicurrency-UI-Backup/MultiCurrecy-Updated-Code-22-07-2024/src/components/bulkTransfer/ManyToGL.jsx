import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import swal from "sweetalert";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import "react-tabs/style/react-tabs.css";
import CustomAlert from "../../layout/CustomAlert";

export default function ManyToGL() {
  const [type, setType] = useState([]);
  const [accounttype, setAccountType] = useState("");
  const [gltype, setGLType] = useState([]);
  const [amount, setAmount] = useState("");
  const [error, setError] = useState("");

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

  // to Account

  useEffect(() => {
    GLAccounttype();
  }, []);

  const GLAccounttype = async () => {
    try {
      const response = await amsApi.get(
        `gl-account-type/getThirdPartyAllowGLAcountTypeList`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setGLType(response.data.glAccountviewModels);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      handleShowError(error);
    }
  };

  //Gl From Account holdernam
  const [data, setData] = useState([]);
  const AccountNumber = data.map((t) => [t.strAccountNumber]);
  sessionStorage.setItem("strAccountNumber", AccountNumber);
  let accountNumber = sessionStorage.getItem("strAccountNumber");

  const strGLAccountDescription = data.map((t) => [t.strGLAccountDescription]);
  sessionStorage.setItem("GLAccountDescription", strGLAccountDescription);
  let glAccountDescription = sessionStorage.getItem("GLAccountDescription");

  const ClosingBalance = data.map((t) => [t.strClosingBalance]);
  sessionStorage.setItem("strClosingBalance", ClosingBalance);
  let closingBalance = sessionStorage.getItem("strClosingBalance");
  useEffect(() => {
    Description();
    setError("");
  }, [accounttype]);
  const Description = async () => {
    try {
      const response = await amsApi.post(
        `gl-account-type/getGLDescrption`,
        {
          strGLAccountType: accounttype,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setData(response.data.glAccountviewModels);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      handleShowError(error);
    }
  };

  // From  GL Account type
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
        // showError(response.data.message);
      }
    } catch (error) {
      handleShowError(error);
    }
  };
  const [isRowAdded, setIsRowAdded] = useState(true);
  const [isValidateButtonDisabled, setIsValidateButtonDisabled] =
    useState(false);
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
    if (accounttype === "" || mainTransferAmount <= 0) {
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
      setIsValidateButtonDisabled(true); // Disable the button
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

  const [showOldButton, setShowOldButton] = useState(true);
  const handleOldButtonClick = () => {
    setIsRowAdded(false);
    setShowOldButton(false);
  };
  // end

  // Submit
  const ManytoGL = async () => {
    if (accounttype === "" || accounttype.length === 0 || amount === "  ") {
      setError(true);
    } else if (!useid) {
      swal("Please check your user id ");
    } else if (!accountNumber) {
      swal("Please check your To GL AccountNo ");
    } else {
      try {
        const response = await amsApi.post(
          `bulkTransfer/manyToGLAccount`,
          {
            strMakerId: useid,
            strToGLAccountNo: accountNumber,
            strToGLAccountType: accounttype,
            strTransferAmount: amount,
            bulkTransferFromAccounts: inputFields,
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
                Many to GL
              </p>
            </div>
          </div>
        </div>
        <div className="overflow-hidden shadow sm:rounded-md mt-1">
          <div className="grid  md:grid-cols-1 ">
            {/* From Account */}
            <div className=" sm:p-2 bg-blue-100 ">
              <h1 className="font-medium flex justify-center  text-black text-2xl">
                To Account Information
              </h1>
              <div className="grid gap-3 mb-1 md:grid-cols-3 lg:grid-cols-5 px-8  mt-1">
                <div>
                  <label
                    htmlFor="GL Account Type"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    GL Account Type <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    id="accountType"
                    name="accountType"
                    value={accounttype}
                    onChange={(e) => setAccountType(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  >
                    <option value="">Select</option>
                    {gltype.map((data) => (
                      <option value={data.strGLAccountType}>
                        {data.strGLAccountType || ""} -
                        {data.strGLAccountDescription || ""}
                      </option>
                    ))}
                  </select>
                  {error && accounttype.length <= 0 ? (
                    <p className="text-red-500   text-xs font-medium">
                      Please Select Account Type !
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div className=" ">
                  {""}
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Account Number
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    // onClick={Description}
                    disabled
                    defaultValue={accountNumber}
                    placeholder="Enter Account number"
                    autoComplete="off"
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  />
                </div>

                <div className="">
                  <label
                    htmlFor="text"
                    className="block text-xs font-semibold text-gray-700"
                  >
                    Description
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    defaultValue={glAccountDescription}
                    disabled
                    autoComplete="off"
                    placeholder="Enter Gl Account Desc.."
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
                    defaultValue={closingBalance}
                    autoComplete="off"
                    placeholder="Enter Available Balance "
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  />
                </div>
                <div className=" text-center">
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
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Enter Amount to Transfer"
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
                </div>
              </div>
            </div>

            {/* from Account type  */}
            <div className="overflow-x-auto relative shadow-md bg-white ">
              <h1 className="font-medium flex justify-center  text-black text-2xl ">
                From Account Information
              </h1>
              {inputFields.map((inputField, index) => (
                <div
                  key={index}
                  className="grid gap-3 mb-1 md:grid-cols-5 px-8 mt-2 "
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
                      name="strFromAccountType"
                      value={inputField.strFromAccountType}
                      onChange={(event) => handleInputChange(index, event)}
                      className="mt-1 p-2 block border w-full shadow-sm sm:text-xs border-gray-300 rounded-md"
                    >
                      <option value="">Select</option>
                      {type.map((data) => (
                        <option value={data.strAccountType}>
                          {data.strAccountType || ""} -
                          {data.strDescription || ""}
                        </option>
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
                      onChange={(event) => handleInputChange(index, event)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Account Number"
                      autoComplete="off"
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
                      disabled
                      onChange={(event) => handleInputChange(index, event)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      autoComplete="off"
                      placeholder="Enter Accountholder name"
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
                      autoComplete="off"
                      onChange={(event) => handleInputChange(index, event)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Amount To Transfer"
                    />
                  </div>

                  <div className="">
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Action
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    {!isRowAdded || index !== inputFields.length - 1 ? (
                      <button
                        type="Add row Here"
                        onClick={handleOldButtonClick}
                        disabled={isRowAdded}
                        className="bg-blue-700 block cursor-not-allowed w-auto px-3 py-1 text-sm text-white bg-clip-padding  border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      >
                        Add Row{""}
                      </button>
                    ) : (
                      <button
                        title="Validate Data"
                        onClick={handleAddFields}
                        disabled={isValidateButtonDisabled}
                        className={`bg-red-700 block w-auto px-3 py-1 text-sm text-white bg-clip-padding border-solid border-gray-500 rounded transition ease-in-out m-0 ${
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

          <div className="bg-gray-100 px-4 py-1 text-right sm:px-2 ">
            <button
              title="Click Submit Button"
              type="button"
              onClick={ManytoGL}
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
