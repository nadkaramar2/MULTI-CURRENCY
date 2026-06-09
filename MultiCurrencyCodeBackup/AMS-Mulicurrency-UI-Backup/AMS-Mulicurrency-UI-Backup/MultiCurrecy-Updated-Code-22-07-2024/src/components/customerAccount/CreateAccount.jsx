import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import Swal from "sweetalert2";
import swal from "sweetalert";
import CustomAlert from "../../layout/CustomAlert";
export default function CreateAccount() {
  const participantId = sessionStorage.getItem("Participantid");
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
  const showSuccess = (resMessage) => {
    Swal.fire({
      title: "Success",
      text: resMessage,
      allowOutsideClick: false,
      icon: "success",
      confirmButtonText: "OK",
    });
  };

  const showError = (resMessage) => {
    Swal.fire({
      text: resMessage,
      allowOutsideClick: false,
      icon: "error",
      title: "Oops...",
      confirmButtonText: "OK",
    });
  };
  const [errorMessage, setErrorMessage] = useState("");
  const [errorMessage1, setErrorMessage1] = useState("");
  const [errorMessage2, setErrorMessage2] = useState("");
  const [errorMessage3, setErrorMessage3] = useState("");
  const [errorMessage4, setErrorMessage4] = useState("");
  const [singledata, setsingledata] = useState([]);
  let Accoumtno = singledata.strLastAccNumber;
  const [alldata, setalldata] = useState([]);
  const [alldata1, setalldata1] = useState([]);
  const [alldata2, setalldata2] = useState([]);
  const [alldata3, setalldata3] = useState([]);
  const [alldata4, setalldata4] = useState([]);
  const [alldata5, setalldata5] = useState([]);
  const [alldata6, setalldata6] = useState([]);
  const [creditTypeData, setcreditTypeData] = useState([]);
  const [customeridCreation, setcustomeridCreation] = useState([]);
  const [countrycodename, setcountrycodename] = useState("");
  const [strCustId, setstrCustId] = useState("");
  const [strTitle, setstrTitle] = useState("");
  const [strEmailID, setstrEmailID] = useState("");
  const [strMobileNo, setstrMobileNo] = useState("");
  const [strPhoneNo, setstrPhoneNo] = useState("");
  const [strAddress1, setstrAddress1] = useState("");
  const [strAddress2, setstrAddress2] = useState("");
  const [strAddress3, setstrAddress3] = useState("");
  const [strCountry, setstrCountry] = useState("");
  const [strState, setstrState] = useState("");
  const [strCity, setstrCity] = useState("");
  const [strPinCode, setstrPinCode] = useState("");
  const [strCreditLimitCategory, setstrCreditLimitCategory] = useState("");
  const [strAddressProofDocumentId, setstrAddressProofDocumentId] =
    useState("");
  const [strAddressProofDocumentValue, setstrAddressProofDocumentValue] =
    useState("");
  const [strIdentityProofDocumentId, setstrIdentityProofDocumentId] =
    useState("");
  const [strIdentityProofDocumentValue, setstrIdentityProofDocumentValue] =
    useState("");
  const [strAccountType, setstrAccountType] = useState("");
  const [strAccountNumber, setstrAccountNumber] = useState("");
  const [dob, setDob] = useState("");
  const [firstName, setFirstName] = useState("");
  const [middleName, setMiddleName] = useState("");
  const [lastName, setLastName] = useState("");
  const [gender, setGender] = useState("");

  const [error, setError] = useState("");
  const handleClick = () => {
    setstrTitle("");
    setcustomeridCreation("");
    setDob("");
    setFirstName("");
    setMiddleName("");
    setLastName("");
    setGender("");
    setstrEmailID("");
    setstrMobileNo("");
    setstrAddress1("");
    setstrCountry("");
    setstrState("");
    setstrCity("");
    setstrPinCode("");
    setstrAddressProofDocumentId("");
    setstrAddressProofDocumentValue("");
    setstrIdentityProofDocumentId("");
    setstrIdentityProofDocumentValue("");
    setstrAccountType("");
    setstrAccountNumber("");
  };
  useEffect(() => {
    categoryListModelsList();
  }, [strAccountType]);
  const categoryListModelsList = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getACTypeListByParticipntWise`,
        {
          strParticipantId: participantId,
        }
      );
      if (response.data.code === "S0000") {
        setalldata(response.data.accountTypeMasterlistData);
      } else {
      }
    } catch (error) {}
  };

  // CategoryAccountType

  useEffect(() => {
    creditType();
  }, []);
  const creditType = async () => {
    try {
      const response = await amsApi.post(
        `credit_limit/getCreditLimitListByParticpantWise`,
        {}
      );
      if (response.data.code === "S0000") {
        setcreditTypeData(response.data.accountCreditLimitCategoriesList);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // 2
  useEffect(() => {
    addressProof();
  }, [strAddressProofDocumentId]);

  const addressProof = async () => {
    try {
      const response = await amsApi.post(
        `address_proof_document_type/documentLst`,
        {
          strParticipantId: participantId,
        }
      );
      if (response.data.code === "S0000") {
        setalldata1(response.data.addressProofDocumentTypeMasters);
      } else {
      }
    } catch (error) {}
  };
  // 3
  useEffect(() => {
    identityProof();
  }, [strIdentityProofDocumentId]);

  const identityProof = async () => {
    try {
      const response = await amsApi.post(
        `identity_proof_document_type/documentLst`,
        {
          strParticipantId: participantId,
        }
      );
      if (response.data.code === "S0000") {
        setalldata2(response.data.identityProofDocumentTypeMasters);
      } else {
      }
    } catch (error) {}
  };
  //4
  useEffect(() => {
    country();
  }, [strCountry]);
  const country = async () => {
    try {
      const response = await amsApi.post(`address/getCountrylist`, {
        strParticipantID: participantId,
      });

      if (response.data.code === "S0000") {
        setalldata3(response.data.countryList);
      } else {
      }
    } catch (error) {}
  };
  //5
  useEffect(() => {
    countrycode();
  }, [countrycodename]);
  const countrycode = async () => {
    try {
      const response = await amsApi.post(`country_code_api/getContryCode`, {});

      if (response.data.code === "S0000") {
        setalldata4(response.data.countryCodeMaster);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };
  // 6

  useEffect(() => {
    state();
    return () => {};
  }, [strCountry]);
  const state = async () => {
    const demoData = alldata3.find((item) => item.countryName === strCountry);
    const countryname = demoData ? demoData.countryId : null;
    try {
      const response = await amsApi.post(`address/getStatelist`, {
        strCountryID: countryname,
      });

      if (response.data.code === "S0000") {
        setalldata5(response.data.stateList);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  useEffect(() => {
    city();
    return () => {};
  }, [alldata5]);

  // 7
  const city = async () => {
    const demoData = alldata5.find((item) => item.strState === strState);
    const statename = demoData ? demoData.stateId : null;
    try {
      const response = await amsApi.post(`address/getCitylist`, {
        strStateID: statename,
      });

      if (response.data.code === "S0000") {
        setalldata6(response.data.cityList);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  useEffect(() => {
    CustomerID();
  }, [strCustId]);
  // // 8
  const CustomerID = async () => {
    if (!strCustId) {
      // swal("Please Check Customer Id ");
    } else {
      try {
        const response = await amsApi.post(`customerId/getCustmerInfo`, {
          strCustId: strCustId,
        });
        if (response.data.code === "S0000") {
          setstrTitle(response.data.customerIdCreation.strTitle);
          setcustomeridCreation(response.data.customerIdCreation);
          setDob(response.data.customerIdCreation.strDOB);
          setFirstName(response.data.customerIdCreation.strFirstName);
          setMiddleName(response.data.customerIdCreation.strMiddleName);
          setLastName(response.data.customerIdCreation.strLastName);
          setGender(response.data.customerIdCreation.strGender);
          setstrEmailID(response.data.customerIdCreation.strEmailID);
          setstrMobileNo(response.data.customerIdCreation.strMobileNo);
          setstrPhoneNo(response.data.customerIdCreation.strPhoneNo);
          setstrAddress1(response.data.customerIdCreation.strAddress1);
          setstrAddress2(response.data.customerIdCreation.strAddress2);
          setstrAddress3(response.data.customerIdCreation.strAddress3);
          setstrCountry(response.data.customerIdCreation.strCountry);
          setstrState(response.data.customerIdCreation.strState);
          setstrCity(response.data.customerIdCreation.strCity);
          setstrPinCode(response.data.customerIdCreation.strPinCode);
          setcountrycodename(response.data.customerIdCreation.strPhoneCode);
          setstrAddressProofDocumentId(
            response.data.customerIdCreation.strAddressProofDocumentId
          );
          setstrAddressProofDocumentValue(
            response.data.customerIdCreation.strAddressProofDocumentValue
          );
          setstrIdentityProofDocumentId(
            response.data.customerIdCreation.strIdentityProofDocumentId
          );
          setstrIdentityProofDocumentValue(
            response.data.customerIdCreation.strIdentityProofDocumentValue
          );
        } else {
          handleShowError("");
          setstrTitle("");
          setcustomeridCreation("");
          setDob("");
          setFirstName("");
          setMiddleName("");
          setLastName("");
          setGender("");
          setstrEmailID("");
          setstrMobileNo("");
          setstrPhoneNo("");
          setstrAddress1("");
          setstrAddress2("");
          setstrAddress3("");
          setstrCountry("");
          setstrState("");
          setstrCity("");
          setstrPinCode("");
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  // save form
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      strTitle === "" ||
      strTitle.length === 0 ||
      firstName === "" ||
      lastName === "" ||
      gender === "" ||
      strEmailID === "" ||
      strAddress1 === "" ||
      strAddressProofDocumentValue === "" ||
      strIdentityProofDocumentValue === "" ||
      strAddressProofDocumentId === "" ||
      strIdentityProofDocumentId === "" ||
      strCountry === "" ||
      strState === ""
    ) {
      setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else {
      try {
        const response = await amsApi.post(`account/saveAccountInfrm`, {
          strCustId: strCustId,
          strParticipantID: participantId,
          strPhoneCode: countrycodename,
          strTitle: strTitle,
          strFirstName: firstName,
          strMiddleName: middleName,
          strLastName: lastName,
          strGender: gender,
          strDOB: dob,
          strEmailID: strEmailID,
          strMobileNo: strMobileNo,
          strPhoneNo: strPhoneNo,
          strAddress1: strAddress1,
          strAddress2: strAddress2,
          strAddress3: strAddress3,
          strCountry: strCountry,
          strState: strState,
          strCity: strCity,
          strPinCode: strPinCode,
          strAddressProofDocumentId: strAddressProofDocumentId,
          strAddressProofDocumentValue: strAddressProofDocumentValue,
          strIdentityProofDocumentId: strIdentityProofDocumentId,
          strIdentityProofDocumentValue: strIdentityProofDocumentValue,
          strAccountType: strAccountType,
          strAccountNumber: Accoumtno,
          strCreditLimitCategory: strCreditLimitCategory,
        });

        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          handleClick();
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  const singleAccount = async () => {
    if (!strAccountType) {
      setError(true);
    } else if (!participantId) {
      swal("Please check your participantID");
    } else {
      try {
        const response = await amsApi.post(
          `/account/getAccountTypeInfoBasedOnAccountType`,
          {
            strParticipantID: participantId,
            strAccountType: strAccountType,
          }
        );
        setsingledata(response.data);
      } catch (error) {
        // showError(error.response.data.message);
      }
    }
  };

  useEffect(() => {
    msctypeSubmit();
  }, [strAccountType]);

  // account type
  const [accountTypeStatus, setaccountTypeStatus] = useState(false);
  const msctypeSubmit = async () => {
    try {
      const response = await amsApi.post(`accountType/isCreditCardType`, {
        strAccountType: strAccountType,
      });

      if (response.status === 200) {
        setaccountTypeStatus(response.data);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-6 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-sm  sm:text-xm text-black :text-2xl  leading-normal ">
                Create Account
              </p>
            </div>
          </div>
        </div>
        <div className="max-w-full mx-auto h-full overflow-auto max-h-full lg:max-h-[30rem]">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-4  sm:p-1 bg-white  ">
                <div className="p-1">
                  <div className="grid gap-2 mb-1 md:grid-cols-4">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Customer Id
                        <span className="text-red-600">*</span>
                      </label>
                      <input
                        type="text"
                        id="strCustId"
                        defaultValue={customeridCreation.strCustId}
                        onChange={(e) => setstrCustId(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      />
                    </div>
                  </div>
                  <div>
                    {""}
                    <h2 className="font-normal md:font-semibold">
                      Personal Information
                    </h2>
                  </div>
                  <div>
                    <div className="grid gap-3 mb-1 md:grid-cols-5 ">
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
                          onChange={(e) => setstrTitle(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value=""> Select </option>
                          <option value="Mr"> Mr.</option>
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
                    <div className="grid gap-3 mb-1 md:grid-cols-3 ">
                      <div>
                        <label
                          htmlFor="strTitle"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          First Name<span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          id="strFirstName"
                          name="strFirstName"
                          minLength={2}
                          value={firstName}
                          // onChange={(e) => setFirstName(e.target.value)}
                          onChange={(e) => {
                            const value = e.target.value;
                            setFirstName(value);

                            const regex = /^[a-zA-Z]+$/;
                            if (!regex.test(value)) {
                              setErrorMessage("Input can't be numeric");
                            } else {
                              setErrorMessage("");
                            }
                            setError("");
                          }}
                          onBlur={() => {
                            if (firstName.length === 0) {
                              setError("Please Enter First Name!");
                              setErrorMessage("");
                            }
                          }}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter First Name"
                        />
                        {errorMessage ? (
                          <p className="text-red-500 text-xs font-medium">
                            {errorMessage}
                          </p>
                        ) : error && firstName.length <= 0 ? (
                          <p className="text-red-500 text-xs font-medium">
                            Please Enter First Name!
                          </p>
                        ) : null}
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Middle Name
                        </label>
                        <input
                          type="text"
                          id="MiddleName"
                          value={middleName}
                          onChange={(e) => setMiddleName(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Middle Name"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Last Name<span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          id="LastName"
                          value={lastName}
                          // onChange={(e) => setLastName(e.target.value)}
                          minLength={2}
                          onChange={(e) => {
                            const value = e.target.value;
                            setLastName(value);

                            const regex = /^[a-zA-Z]+$/;
                            if (!regex.test(value)) {
                              setErrorMessage1("Input can't be numeric");
                            } else {
                              setErrorMessage1("");
                            }
                            setError("");
                          }}
                          onBlur={() => {
                            if (lastName.length === 0) {
                              setError("Please Enter Last Name!");
                              setErrorMessage1("");
                            }
                          }}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Last name"
                        />
                        {errorMessage1 ? (
                          <p className="text-red-500 text-xs font-medium">
                            {errorMessage1}
                          </p>
                        ) : error && lastName.length <= 0 ? (
                          <p className="text-red-500 text-xs font-medium">
                            Please Enter Last Name!
                          </p>
                        ) : null}
                        {/* {error && lastName.length <= 0 ? (
                            <p className="text-red-500   text-xs font-medium">
                              Please Enter Last Name
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
                          Gender<span className="text-red-600">*</span>
                        </label>
                        <select
                          id="gender"
                          name="gender"
                          value={gender}
                          onChange={(e) => setGender(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value=""> Select </option>
                          <option value="Male">Male</option>
                          <option value="Female">Female</option>
                        </select>
                        {error && gender.length <= 0 ? (
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
                          Date Of Birth
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          id="dob"
                          value={dob}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Date of Birth"
                        />
                        {error && dob.length <= 0 ? (
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
                          Email Id<span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          id="strEmailID"
                          value={strEmailID}
                          // onChange={(e) => setstrEmailID(e.target.value)}
                          onChange={(e) => {
                            const value = e.target.value;
                            setstrEmailID(value);

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
                          placeholder="John12@Email.com"
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
                          Country Code
                          <span className="text-red-600">*</span>
                        </label>
                        <select
                          id="countrycodename"
                          name="countrycodename"
                          value={countrycodename}
                          onChange={(e) => setcountrycodename(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value="">Select</option>
                          {alldata4.map((data) => (
                            <option
                              className="capatlize text-sm"
                              value={data.phonecode}
                            >
                              {data.country_code}
                            </option>
                          ))}
                        </select>
                        {error && countrycodename.length <= 0 ? (
                          <p className="text-red-500   text-xs font-medium">
                            Please Select Country Code
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
                          Mobile Number
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          id="strMobileNo"
                          minLength={10}
                          maxLength={12}
                          value={strMobileNo}
                          // onChange={(e) => setstrMobileNo(e.target.value)}
                          onChange={(e) => {
                            const value = e.target.value;
                            setstrMobileNo(value);

                            const regex = /^[0-9]*$/;
                            if (!regex.test(value)) {
                              setErrorMessage2(" Only allow numeric digits");
                            } else {
                              setErrorMessage2("");
                            }
                            setError("");
                          }}
                          onBlur={() => {
                            if (strMobileNo.length === 0) {
                              setError("Please Enter Mobile No!");
                              setErrorMessage2("");
                            }
                          }}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Mobile Number"
                        />
                        {errorMessage2 ? (
                          <p className="text-red-500 text-xs font-medium">
                            {errorMessage2}
                          </p>
                        ) : error && strMobileNo.length <= 0 ? (
                          <p className="text-red-500 text-xs font-medium">
                            Please Enter Mobile No!
                          </p>
                        ) : null}
                        {/* {error && strMobileNo.length <= 0 ? (
                            <p className="text-red-500   text-xs font-medium">
                              Please Enter Mobile Number
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
                          Phone Number
                        </label>
                        <input
                          type="text"
                          id="strPhoneNo"
                          value={strPhoneNo}
                          onChange={(e) => setstrPhoneNo(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Phone Number"
                        />
                      </div>
                    </div>
                    <div>
                      <h2 className="font-normal md:font-semibold">
                        Address Information
                      </h2>
                    </div>
                    <div className="grid gap-3 mb-1 md:grid-cols-3 ">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Address Line1
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          id="strAddress1"
                          value={strAddress1}
                          onChange={(e) => setstrAddress1(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Address Line1"
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
                          Address Line2
                        </label>
                        <input
                          type="text"
                          id="strAddress2"
                          value={strAddress2}
                          onChange={(e) => setstrAddress2(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Address Line2"
                        />
                      </div>

                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Address Line3
                        </label>
                        <input
                          type="text"
                          id="strAddress3"
                          value={strAddress3}
                          onChange={(e) => setstrAddress3(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Address Line3"
                        />
                      </div>
                    </div>
                    <div className="grid gap-3 mb-1 md:grid-cols-3 ">
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
                          onChange={(e) => setstrCountry(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value="india">Select</option>
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
                            Please Select Country Name
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
                          onChange={(e) => setstrState(e.target.value)}
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
                            Please Select State Name
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
                          City<span className="text-red-600">*</span>
                        </label>
                        <select
                          id="strCity"
                          name="strCity"
                          value={strCity}
                          onChange={(e) => setstrCity(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value="">Select</option>
                          {alldata6.map((data) => (
                            <option
                              className="capatlize text-sm"
                              value={data.cityName}
                            >
                              {data.cityName}
                            </option>
                          ))}
                        </select>
                        {error && strCity.length <= 0 ? (
                          <p className="text-red-500   text-xs font-medium">
                            Please Select City Name
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
                          maxLength={6}
                          minLength={6}
                          value={strPinCode}
                          // onChange={(e) => setstrPinCode(e.target.value)}
                          onChange={(e) => {
                            const value = e.target.value;
                            setstrPinCode(value);

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
                            if (strPinCode.length === 0) {
                              setError("Please Enter Postal Code!");
                              setErrorMessage3("");
                            }
                          }}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Postal Cod"
                        />
                        {errorMessage3 ? (
                          <p className="text-red-500 text-xs font-medium">
                            {errorMessage3}
                          </p>
                        ) : error && strPinCode.length <= 0 ? (
                          <p className="text-red-500 text-xs font-medium">
                            Please Enter Postal Code!
                          </p>
                        ) : null}
                      </div>
                    </div>
                    <div>
                      {""}
                      <h2 className="font-normal md:font-semibold ">
                        Address Proof(POA)
                      </h2>
                    </div>
                    <div className="grid gap-3 mb-1 md:grid-cols-3 ">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Document Type
                          <span className="text-red-600">*</span>
                        </label>
                        <select
                          name="strAddressProofDocumentId"
                          id="strAddressProofDocumentId"
                          value={strAddressProofDocumentId}
                          onChange={(e) =>
                            setstrAddressProofDocumentId(e.target.value)
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
                            Please Select Document Type
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
                          Document Value
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          id="strAddressProofDocumentValue"
                          value={strAddressProofDocumentValue}
                          onChange={(e) =>
                            setstrAddressProofDocumentValue(e.target.value)
                          }
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Document Value"
                        />
                        {error && strAddressProofDocumentValue.length <= 0 ? (
                          <p className="text-red-500   text-xs font-medium">
                            Please Enter Document Value
                          </p>
                        ) : (
                          ""
                        )}
                      </div>
                    </div>
                    <div>
                      {""}
                      <h2 className="font-normal md:font-semibold ">
                        Identification Proof(POI)
                      </h2>
                    </div>
                    <div className="grid gap-3 mb-1 md:grid-cols-3 ">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Document Type
                          <span className="text-red-600">*</span>
                        </label>
                        <select
                          name="strIdentityProofDocumentId"
                          id="strIdentityProofDocumentId"
                          value={strIdentityProofDocumentId}
                          onChange={(e) =>
                            setstrIdentityProofDocumentId(e.target.value)
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
                            Please Select Document Type
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
                          Document Value
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          id="strIdentityProofDocumentValue"
                          value={strIdentityProofDocumentValue}
                          onChange={(e) =>
                            setstrIdentityProofDocumentValue(e.target.value)
                          }
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Document Value"
                        />
                        {error && strIdentityProofDocumentValue.length <= 0 ? (
                          <p className="text-red-500   text-xs font-medium">
                            Please Enter Document Value
                          </p>
                        ) : (
                          ""
                        )}
                      </div>
                    </div>
                    <div>
                      {""}
                      <h2 className="font-normal md:font-semibold">
                        Account Information
                      </h2>
                    </div>
                    <div className="grid gap-3 mb-1 md:grid-cols-3 ">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Account Type
                          <span className="text-red-600">*</span>
                        </label>
                        <select
                          name="strAccountType"
                          id="strAccountType"
                          value={strAccountType}
                          onChange={(e) => setstrAccountType(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value="">Select</option>
                          {alldata.map((data) => (
                            <option
                              className="capatlize text-sm"
                              value={data.strAccountType}
                            >
                              {data.strAccountType}-{data.strDescription}
                            </option>
                          ))}
                        </select>
                        {error && strAccountType.length <= 0 ? (
                          <p className="text-red-500   text-xs font-medium">
                            Please Select Account Type
                          </p>
                        ) : (
                          ""
                        )}
                      </div>
                      <div onClick={singleAccount}>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Account Number
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          id="strLastAccNumber"
                          //defaultValue={Accoumtno}
                          value={Accoumtno}
                          onChange={(e) => setstrAccountNumber(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          autoComplete="off"
                        />
                      </div>
                      {accountTypeStatus === true && (
                        <div>
                          <label
                            htmlFor="text"
                            className="block text-xs font-semibold text-gray-700"
                          >
                            Credit Type
                            <span className="text-red-600">*</span>
                          </label>
                          <select
                            name="strCreditLimitCategory"
                            id="strCreditLimitCategory"
                            value={strCreditLimitCategory}
                            onChange={(e) =>
                              setstrCreditLimitCategory(e.target.value)
                            }
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          >
                            <option value="">Select</option>
                            {creditTypeData.map((data) => (
                              <option
                                className="capatlize text-sm"
                                value={data.strCreditType}
                              >
                                {data.strCreditType}
                              </option>
                            ))}
                          </select>
                          {error && strCreditLimitCategory.length <= 0 ? (
                            <p className="text-red-500   text-xs font-medium">
                              Please Select Credit type
                            </p>
                          ) : (
                            ""
                          )}
                        </div>
                      )}
                    </div>
                  </div>
                </div>
                {/* ))} */}
              </div>

              <div className="bg-gray-100 px-4 py-4 text-right sm:px-6 ">
                <button
                  title="Click Submit Button"
                  type="button"
                  data-modal-toggle="defaultModal"
                  onClick={handleSubmit}
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-4  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-4 mx-4 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
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
    </AppLayout>
  );
}
