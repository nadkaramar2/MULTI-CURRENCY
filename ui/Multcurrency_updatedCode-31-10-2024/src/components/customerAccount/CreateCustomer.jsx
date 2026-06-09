import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";

import "react-datepicker/dist/react-datepicker.css";

// CSS Modules, react-datepicker-cssmodules.css//
import "react-datepicker/dist/react-datepicker-cssmodules.css";
import CustomAlert from "../../layout/CustomAlert";
export default function CreateCustomer() {
  const participantID = sessionStorage.getItem("Participantid");
  const [alldata1, setalldata1] = useState([]);
  const [alldata2, setalldata2] = useState([]);
  const [alldata3, setalldata3] = useState([]);
  const [alldata4, setalldata4] = useState([]);
  const [alldata5, setalldata5] = useState([]);
  const [alldata6, setalldata6] = useState([]);
  const [strAddressProofDocumentId, setStrAddressProofDocumentId] =
    useState("");
  const [strCountry, setStrCountry] = useState("");
  const [strIdentityProofDocumentId, setStrIdentityProofDocumentId] =
    useState("");

  const [strState, setStrState] = useState("");
  // const [strCity, setstrCity] = useState("");
  const [strTitle, setStrTitle] = useState("");
  const [strFirstName, setStrFirstName] = useState("");
  const [strMiddleName, setStrMiddleName] = useState("");
  const [strLastName, setStrLastName] = useState("");
  const [strGender, settrGender] = useState("");
  const [strDOB, setStrDOB] = useState("");
  const [strEmailID, setStrEmailID] = useState("");
  const [mobileNo, setStrMobileNo] = useState("");
  const [strAddress1, setStrAddress1] = useState("");
  const [strAddress2, setStrAddress2] = useState("");
  const [strAddress3, setStrAddress3] = useState("");
  const [strPhoneCode, setStrPhoneCode] = useState("");
  const [strPinCode, setStrPinCode] = useState("");
  const [strCity, setStrCity] = useState("");

  const [strAddressProofDocumentValue, setStrAddressProofDocumentValue] =
    useState("");
  const [strIdentityProofDocumentValue, setStrIdentityProofDocumentValue] =
    useState("");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [errorMessage1, setErrorMessage1] = useState("");
  const [errorMessage2, setErrorMessage2] = useState("");
  const [errorMessage3, setErrorMessage3] = useState("");
  const [errorMessage4, setErrorMessage4] = useState("");
  const handleClick = () => {
    setStrTitle("");
    setStrFirstName("");
    setStrMiddleName("");
    setStrLastName("");
    settrGender("");
    setStrDOB("");
    setStrAddress3("");
    setStrEmailID("");
    setStrMobileNo("");
    setStrAddress1("");
    setStrAddress2("");
    setStrPhoneCode("");
    setStrPinCode("");
    setStrAddressProofDocumentValue("");
    setStrIdentityProofDocumentValue("");
    setStrAddressProofDocumentId("");
    setStrIdentityProofDocumentId("");
    setStrState("");
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

  // issues here
  useEffect(() => {
    addressProof();
  }, [strAddressProofDocumentId]);

  const addressProof = async () => {
    try {
      const response = await amsApi.post(
        `address_proof_document_type/documentLst`,
        {
          strParticipantId: participantID,
        }
      );
      if (response.data.code === "S0000") {
        setalldata1(response.data.addressProofDocumentTypeMasters);
      } else {
        // showSuccess(response.data.message);
      }
    } catch (error) {}
  };

  useEffect(() => {
    identityProof();
  }, [strIdentityProofDocumentId]);
  const identityProof = async () => {
    try {
      const response = await amsApi.post(
        `identity_proof_document_type/documentLst`,
        {
          strParticipantId: participantID,
        }
      );

      if (response.data.code === "S0000") {
        setalldata2(response.data.identityProofDocumentTypeMasters);
      } else {
        // shpwError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  useEffect(() => {
    country();
  }, [strCountry]);
  const country = async () => {
    try {
      const response = await amsApi.post(`address/getCountrylist`, {
        strParticipantID: participantID,
      });

      if (response.data.code === "S0000") {
        setalldata3(response.data.countryList);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };
  useEffect(() => {
    countrycode();
  }, []);
  const countrycode = async () => {
    try {
      const response = await amsApi.post(`country_code_api/getContryCode`, {});

      if (response.data.code === "S0000") {
        setalldata4(response.data.countryCodeMaster);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };

  const state = async (data) => {
    const demoData = alldata3.find((item) => item.countryName === data);
    const countryname = demoData ? demoData.countryId : null;
    try {
      const response = await amsApi.post(`address/getStatelist`, {
        strCountryID: countryname,
      });

      if (response.data.code === "S0000") {
        setalldata5(response.data.stateList);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };

  const city = async (data) => {
    const demoData = alldata5.find((item) => item.strState === data);
    const statename = demoData ? demoData.stateId : null;
    try {
      const response = await amsApi.post(`address/getCitylist`, {
        strStateID: statename,
      });

      if (response.data.code === "S0000") {
        setalldata6(response.data.cityList);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };

  // SAVE FORM
  const CreateCustomerSubmit = async () => {
    if (
      strTitle === "" ||
      strTitle.length === 0 ||
      strFirstName === "" ||
      strLastName === "" ||
      strGender === "" ||
      strDOB === "" ||
      strEmailID === "" ||
      mobileNo === "" ||
      strAddress1 === "" ||
      strPhoneCode === "" ||
      strAddressProofDocumentValue === "" ||
      strIdentityProofDocumentValue === "" ||
      strAddressProofDocumentId === "" ||
      strIdentityProofDocumentId === "" ||
      strCountry === "" ||
      strState === "  "
    ) {
      setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else {
      try {
        const response = await amsApi.post(`customerId/saveCustomer`, {
          strTitle: strTitle,
          strParticipantID: participantID,
          strFirstName: strFirstName,
          strMiddleName: strMiddleName,
          strLastName: strLastName,
          strGender: strGender,
          strDOB: strDOB,
          strPhoneCode: strPhoneCode.split("+")[1],
          strEmailID: strEmailID,
          strMobileNo: mobileNo,
          strAddress1: strAddress1,
          address2: strAddress2,
          address3: strAddress3,
          strCountry: strCountry,
          strState: strState,
          strCity: strCity,
          strPinCode: strPinCode,
          strAddressProofDocumentId: strAddressProofDocumentId,
          strAddressProofDocumentValue: strAddressProofDocumentValue,
          strIdentityProofDocumentId: strIdentityProofDocumentId,
          strIdentityProofDocumentValue: strIdentityProofDocumentValue,
        });
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          setStrTitle("");
          setStrFirstName("");
          setStrMiddleName("");
          setStrLastName("");
          settrGender("");
          setStrDOB("");
          setStrEmailID("");
          setStrMobileNo("");
          setStrAddress1("");
          setStrAddress2("");
          setStrPhoneCode("");
          setStrAddressProofDocumentValue("");
          setStrIdentityProofDocumentValue("");
          setStrAddressProofDocumentId("");
          setStrIdentityProofDocumentId("");
          setStrState("");
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
      <>
        <div className="min-h-full first-line:flex items-center justify-center border-b sticky top-0">
          <div className=" bg-blue-200 rounded-md shadow-2x py-2 mb-4">
            <h1 className="mx-auto flex justify-start px-2 text-white text-2xl font-semibold">
              <p className="flex justify-start text-sm  sm:text-x]m text-black :text-2xl  leading-normal">
                Create Customer
              </p>
            </h1>
          </div>
        </div>
        <div className="max-w-7xl mx-auto h-full overflow-auto max-h-full lg:max-h-[28rem]">
          <div className="mt-4 md:mt-0 md:col-span-2 ">
            <div className="shadow overflow-hidden sm:rounded-md">
              <div className="sm:p-1 bg-white  ">
                <div className="grid gap-2 md:grid-cols-3 lg:grid-cols-5 ">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Title<span className="text-red-600">*</span>
                    </label>
                    <select
                      id="strTitle"
                      name="strTitle"
                      value={strTitle}
                      onChange={(e) => setStrTitle(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value=""> Select </option>
                      <option value="Mr">Mr.</option>
                      <option value="Mrs">Mrs.</option>
                    </select>
                    {error && strTitle.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Title
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
                <div className="grid gap-2 mb-2 md:grid-cols-3 lg:grid-cols-3 py-1 ">
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      First Name<span className="text-red-600">*</span>
                    </label>
                    <input
                      type="text"
                      id="strFirstName"
                      value={strFirstName}
                      minLength={2}
                      // onChange={(e) => setStrFirstName(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setStrFirstName(value);

                        const regex = /^[a-zA-Z]+$/;
                        if (!regex.test(value)) {
                          setErrorMessage("Input can't be numeric");
                        } else {
                          setErrorMessage("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (strFirstName.length === 0) {
                          setError("Please Enter First Name!");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter First number"
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage}
                      </p>
                    ) : error && strFirstName.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter First Name!
                      </p>
                    ) : null}
                    {/* {error && strFirstName.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter First Name
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>
                  <div>
                    <label
                      htmlFor="GL Account Description"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Middle Name
                    </label>
                    <input
                      type="text"
                      id="strMiddleName"
                      value={strMiddleName}
                      onChange={(e) => setStrMiddleName(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter middle name"
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Last Name<span className="text-red-600">*</span>
                    </label>
                    <input
                      type="text"
                      id="strLastName"
                      value={strLastName}
                      // onChange={(e) => setStrLastName(e.target.value)}
                      minLength={2}
                      onChange={(e) => {
                        const value = e.target.value;
                        setStrLastName(value);

                        const regex = /^[a-zA-Z]+$/;
                        if (!regex.test(value)) {
                          setErrorMessage1("Input can't be numeric");
                        } else {
                          setErrorMessage1("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (strLastName.length === 0) {
                          setError("Please Enter Last Name!");
                          setErrorMessage1("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter last name"
                    />
                    {errorMessage1 ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage1}
                      </p>
                    ) : error && strLastName.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Last Name!
                      </p>
                    ) : null}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Gender<span className="text-red-600">*</span>
                    </label>
                    <select
                      id="strGender"
                      name="strGender"
                      value={strGender}
                      onChange={(e) => settrGender(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      required
                    >
                      <option value=""> Select</option>
                      <option value="Male">Male</option>
                      <option value="Female">Female</option>
                    </select>
                    {error && strGender.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Gender
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Date Of Birth<span className="text-red-600">*</span>
                    </label>

                    <input
                      type="text"
                      id="strDOB"
                      name="strDOB"
                      value={strDOB}
                      onChange={(e) => {
                        setStrDOB(e.target.value);
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="eg:- 2000/08/05"
                    />

                    {error && strDOB.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Date Of Birth
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="md:col-span-2 lg:col-span-1">
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Email Id<span className="text-red-600">*</span>
                    </label>
                    <input
                      type="text"
                      id="strEmailID"
                      value={strEmailID}
                      // onChange={(e) => setStrEmailID(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setStrEmailID(value);

                        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                        if (!regex.test(value)) {
                          setErrorMessage4(
                            " Invalid Mail Id"
                            // "Please enter only numeric values"
                          );
                        } else {
                          setErrorMessage4("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (strEmailID.length === 0) {
                          setError("Please Enter Email ID!");
                          setErrorMessage4("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Jhon12@Gmail.com"
                    />
                    {errorMessage4 ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage4}
                      </p>
                    ) : error && strEmailID.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Email ID!
                      </p>
                    ) : null}
                    {/* {error && strEmailID.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Email ID
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Country Code<span className="text-red-600">*</span>
                    </label>
                    <select
                      id="strPhoneCode"
                      name="strPhoneCode"
                      value={strPhoneCode}
                      onChange={(e) => setStrPhoneCode(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata4.map((data) => (
                        <option
                          className="capatlize text-sm"
                          value={data.country_code}
                        >
                          {data.country_code}
                        </option>
                      ))}
                    </select>
                    {error && strPhoneCode.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter CountryCode
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="col-span-1">
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Mobile Number<span className="text-red-600">*</span>
                    </label>
                    <input
                      type="text"
                      id="mobileNo"
                      name="mobileNo"
                      minLength={10}
                      maxLength={12}
                      placeholder="Enter mobile number"
                      value={mobileNo}
                      // onChange={(e) => setStrMobileNo(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setStrMobileNo(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage2(" Only allow numeric digits");
                        } else {
                          setErrorMessage2("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (mobileNo.length === 0) {
                          setError("Please Enter Mobile No!");
                          setErrorMessage2("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                    {errorMessage2 ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage2}
                      </p>
                    ) : error && mobileNo.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Mobile No!
                      </p>
                    ) : null}
                    {/* {error && mobileNo.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Mobile No
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>
                  <div className="col-span-1">
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Phone Number
                    </label>
                    <input
                      type="text"
                      id="phoneNo"
                      name="phoneNo"
                      minLength={10}
                      maxLength={12}
                      placeholder="Enter Phone number"
                      value={mobileNo}
                      // onChange={(e) => setStrMobileNo(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setStrMobileNo(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage2(" Only allow numeric digits");
                        } else {
                          setErrorMessage2("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (mobileNo.length === 0) {
                          setError("Please Enter Mobile No!");
                          setErrorMessage2("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                    {errorMessage2 ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage2}
                      </p>
                    ) : error && mobileNo.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Mobile No!
                      </p>
                    ) : null}
                    {/* {error && mobileNo.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Mobile No
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>
                </div>
                <div>
                  <h2 className="font-normal md:font-semibold ">
                    Address Information
                  </h2>
                </div>
                <div className="grid gap-3 mb-1 md:grid-cols-2 lg:grid-cols-3">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Address Line 1<span className="text-red-600">*</span>
                    </label>
                    <input
                      type="text"
                      id="strAddress1"
                      value={strAddress1}
                      onChange={(e) => setStrAddress1(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Address1"
                    />
                    {error && strAddress1.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Address1
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Address Line 2
                    </label>
                    <input
                      type="text"
                      id="strAddress2"
                      value={strAddress2}
                      onChange={(e) => setStrAddress2(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Address2"
                    />
                  </div>

                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Address Line 3
                    </label>
                    <input
                      type="text"
                      id="strAddress3"
                      value={strAddress3}
                      onChange={(e) => setStrAddress3(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter address3"
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Country<span className="text-red-600">*</span>
                    </label>
                    <select
                      name="strCountry"
                      id="strCountry"
                      value={strCountry}
                      onChange={(e) =>
                        setStrCountry(e.target.value, state(e.target.value))
                      }
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata3.map((data) => (
                        <option
                          className="capatlize text-sm"
                          value={data.countryName}
                        >
                          {data.countryName}
                        </option>
                      ))}
                    </select>
                    {error && strCountry.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Country
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      State<span className="text-red-600">*</span>
                    </label>
                    <select
                      id="strState"
                      name="strState"
                      value={strState}
                      onChange={(e) =>
                        setStrState(e.target.value, city(e.target.value))
                      }
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata5.map((data) => (
                        <option
                          className="capatlize text-sm"
                          value={data.strState}
                        >
                          {data.strState}
                        </option>
                      ))}
                    </select>
                    {error && strState.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select State
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      City
                      <span className="text-red-600">*</span>
                    </label>
                    <select
                      id="strCity"
                      name="strCity"
                      value={strCity}
                      onChange={(e) => setStrCity(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      required
                    >
                      <option value="">Select</option>
                      {alldata6.map((data) => (
                        <option
                          className="capatlize text-lg"
                          value={data.cityName}
                        >
                          {data.cityName}
                        </option>
                      ))}
                    </select>
                    {error && strCity.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select City
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Postal Code<span className="text-red-600">*</span>
                    </label>
                    <input
                      type="text"
                      id="strPinCode"
                      name="strPinCode"
                      maxLength={6}
                      minLength={6}
                      value={strPinCode}
                      // onChange={(e) => setStrPinCode(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setStrPinCode(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage3(
                            " Only allow numeric digits"
                            // "Please enter only numeric values"
                          );
                        } else {
                          setErrorMessage3("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (strPhoneCode.length === 0) {
                          setError("Please Enter Postal Code!");
                          setErrorMessage3("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter postal code"
                    />
                    {errorMessage3 ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage3}
                      </p>
                    ) : error && strPhoneCode.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Postal Code!
                      </p>
                    ) : null}
                    {/* {error && strPhoneCode.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Postal Code
                      </p>
                    ) : (
                      ""
                    )} */}
                  </div>
                </div>
                <div>
                  {""}
                  <h2 className="font-normal md:font-semibold">
                    Address Proof(POA)
                  </h2>
                </div>
                <div className="grid gap-3 mb-1 md:grid-cols-2 ">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Document Type<span className="text-red-600">*</span>
                    </label>
                    <select
                      name="strAddressProofDocumentId"
                      id="strAddressProofDocumentId"
                      value={strAddressProofDocumentId}
                      onChange={(e) =>
                        setStrAddressProofDocumentId(e.target.value)
                      }
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata1.map((data) => (
                        <option
                          className="capatlize text-sm"
                          // value={}
                        >
                          {data.strDocumentDescr}
                        </option>
                      ))}
                    </select>
                    {error && strAddressProofDocumentId.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select AddressProofDocumentId
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Document Value<span className="text-red-600">*</span>
                    </label>
                    <input
                      type="text"
                      id="strAddressProofDocumentValue"
                      placeholder="Enter AddressProofDoumentValue"
                      value={strAddressProofDocumentValue}
                      onChange={(e) =>
                        setStrAddressProofDocumentValue(e.target.value)
                      }
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    />
                    {error && strAddressProofDocumentValue.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter AddressProofDocumentValue
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
                <div>
                  {""}
                  <h2 className="font-normal md:font-bold py-1">
                    Identification Proof(POI)
                  </h2>
                </div>
                <div className="grid gap-4 mb-1 md:grid-cols-2">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Document Type<span className="text-red-600">*</span>
                    </label>
                    <select
                      name="strIdentityProofDocumentId"
                      id="strIdentityProofDocumentId"
                      value={strIdentityProofDocumentId}
                      onChange={(e) =>
                        setStrIdentityProofDocumentId(e.target.value)
                      }
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata2.map((data) => (
                        <option
                          className="capatlize text-sm"
                          // value={}
                        >
                          {data.strDocumentDescr}
                        </option>
                      ))}
                    </select>
                    {error && strIdentityProofDocumentId.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Identity Proof Document Id
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Document Value<span className="text-red-600">*</span>
                    </label>
                    <input
                      type="text"
                      id="strIdentityProofDocumentValue"
                      value={strIdentityProofDocumentValue}
                      onChange={(e) =>
                        setStrIdentityProofDocumentValue(e.target.value)
                      }
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Identity Proof Document Value"
                    />
                    {error && strIdentityProofDocumentValue.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Identity Proof Document Value
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>
              <div className="px-4 py-2 text-right sm:px-6 bg-gray-200">
                <button
                  title="Click Submit Button"
                  type="button"
                  onClick={CreateCustomerSubmit}
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3 mx-4 text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-red-400 py-1.5 px-3 mx-4 text-xs font-medium text-white shadow-sm hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2"
                >
                  Clear
                </button>
              </div>
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
      </>
    </AppLayout>
  );
}
