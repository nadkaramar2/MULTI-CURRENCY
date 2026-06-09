import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import { useNavigate, useParams } from "react-router-dom";
import CustomAlert from "../../layout/CustomAlert";

export default function EditAccount() {
  const navigate = useNavigate();
  const { strAccountType } = useParams();
  const { strAccountNumber } = useParams();
  const [alldata5, setalldata5] = useState([]);
  const [alldata6, setalldata6] = useState([]);
  const [alldata3, setalldata3] = useState([]);
  const [alldata1, setalldata1] = useState([]);

  const [alldatatype, setalldatatype] = useState([]);
  const [editAccount, seteditAccount] = useState([]);
  const [editcategory, seteditcategory] = useState([]);
  let participantID = sessionStorage.getItem("Participantid");
  const [strCountry, setstrCountry] = useState("");
  const [strState, setstrState] = useState("");
  const [strCity, setstrCity] = useState("");
  const [strAddressProofDocumentId, setStrAddressProofDocumentId] =
    useState("");
  const [strCreditlimit, setstrCreditlimit] = useState("");

  const [strFirstName, setstrFirstName] = useState("");
  const [strMiddleName, setstrMiddleName] = useState("");
  const [strDOB, setstrDOB] = useState("");
  const [strEmailID, setstrEmailID] = useState("");
  const [strMobileNo, setstrMobileNo] = useState("");
  const [strPhoneNo, setstrPhoneNo] = useState("");
  const [strAddress1, setstrAddress1] = useState("");
  const [strAddress2, setstrAddress2] = useState("");
  const [strAddress3, setstrAddress3] = useState("");
  const [strCountryCode, setstrCountryCode] = useState("");
  const [strPinCode, setstrPinCode] = useState("");
  const [strAddressProofDocumentValue, setstrAddressProofDocumentValue] =
    useState("");
  const [strIdentityProofDocumentValue, setstrIdentityProofDocumentValue] =
    useState("");

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
        seteditAccount(response.data.addressProofDocumentTypeMasters);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
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
        // handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(
        "Sorry, the server is under mentaines. Please try again later"
      );
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
      handleShowError(
        "Sorry, the server is under mentaines. Please try again later"
      );
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
      handleShowError(
        "Sorry, the server is under mentaines. Please try again later"
      );
    }
  };

  useEffect(() => {
    handleView();
  }, []);

  const handleView = async (event) => {
    try {
      const response = await amsApi.post(
        `accountType/getAccountTypeMasterDetailsBasedonAccountType`,
        {
          strAccountType: strAccountType,
        }
      );
      if (response.data.code === "S0000") {
        setalldatatype(response.data.accountTypeMaster2);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(
        "Sorry, the server is under mentaines. Please try again later"
      );
    }
  };
  useEffect(() => {
    handleSubmitView();
  }, []);

  const handleSubmitView = async (event) => {
    try {
      const response = await amsApi.post(
        `account/getAccountInfoLstByAccountNo`,
        {
          strAccountType: strAccountType,
          strAccountNumber: strAccountNumber,
        }
      );
      if (response.data.code === "S0000") {
        seteditAccount(response.data.accountInfoList);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  useEffect(() => {
    credilimitCategory();
  }, []);

  const credilimitCategory = async (event) => {
    try {
      const response = await amsApi.post(
        `credit_limit/getCreditLimitListByParticpantWise`,
        {
          strParticipantId: participantID,
        }
      );
      if (response.data.code === "S0000") {
        seteditcategory(response.data.accountCreditLimitCategoriesList);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  const editAccountDetails = async () => {
    try {
      const response = await amsApi.post(`/account/updateInstantAcountInfo`, {
        strIsInstantAccount: "N",
        strAccountType: strAccountType,
        strAccountNumber: strAccountNumber,
        strFirstName: strFirstName,
        strMiddleName: strMiddleName,
        strDOB: strDOB,
        strEmailID: strEmailID,
        strMobileNo: strMobileNo,
        strPhoneNo: strPhoneNo,
        strAddress1: strAddress1,
        strAddress2: strAddress2,
        strAddress3: strAddress3,
        strCountryCode: strCountryCode,
        strState: strState,
        strCity: strCity,
        strPinCode: strPinCode,
        strAddressProofDocumentValue: strAddressProofDocumentValue,
        strIdentityProofDocumentValue: strIdentityProofDocumentValue,
      });
      if (response.data.code === "S0000") {
        handleShowSuccess(response.data.message);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(
        "Sorry, the server is under mentaines. Please try again later"
      );
    }
  };

  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Edit Account Creation
              </p>
            </div>
          </div>
        </div>

        <div className="max-w-7xl mx-auto h-full overflow-auto max-h-full lg:max-h-[30rem]">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-4  sm:p-2 bg-white  ">
                {editAccount.map((data) => (
                  <div>
                    <div className="grid gap-4 mb-2 md:grid-cols-3">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Account Type<span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          defaultValue={strAccountType}
                          // disabled
                          // value={data.strAccountType}
                          // onChange={(e) => setstrAccountType(e.target.value)}
                          id="strAccountType"
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Account Number
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDescription}
                          defaultValue={strAccountNumber}
                          // onChange={(e) => setstrDescription(e.target.value)}
                          id="strDescription"
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>

                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Credit Limit Category
                          <span className="text-red-600">*</span>
                        </label>
                        <select
                          name="strCreditlimit"
                          id="strCreditlimit"
                          value={strCreditlimit}
                          onChange={(e) => setstrCreditlimit(e.target.value)}
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value=""> Select </option>
                          {editcategory.map((data) => (
                            <optgroup key={uuidv4()}>
                              <option
                                className="capatlize text-lg"
                                value={data.strCreditType}
                                //onChange={setstrState(data.countryId)}
                              >
                                {data.strCreditType}
                              </option>
                            </optgroup>
                          ))}
                        </select>
                      </div>
                    </div>

                    {/* <div>
                        {""}
                        <h2 className="font-normal md:font-bold">
                          Personal Information
                        </h2>
                      </div> */}
                    <div>
                      {""}
                      <h2 className="font-normal md:font-bold mb-1">
                        Personal Information
                      </h2>
                      {/* <br /> */}
                    </div>
                    <div className="grid gap-4 mb-1 md:grid-cols-5">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Title
                          <span className="text-red-600">*</span>
                        </label>
                        <select
                          id="strStatus"
                          name="strStatus"
                          defaultValue={data.strTitle}
                          // value={strStatus}
                          // onChange={(e) => setstrStatus(e.target.value)}
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value=""> Select</option>
                          <option value="Mr"> Mr.</option>
                          <option value="Mrs">Mrs </option>
                          {/* <option className="captlize text-lg"></option> */}
                        </select>
                      </div>
                    </div>
                    <div className="grid gap-6 mb-6 md:grid-cols-3">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          First Name
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          id="strFirstName"
                          // value={strDormancyPeriodsInDays}
                          defaultValue={data.strFirstName}
                          onChange={(e) => setstrFirstName(e.target.value)}
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Middle Name
                        </label>
                        <input
                          type="text"
                          id="strMiddleName"
                          defaultValue={data.strMiddleName}
                          // value={strDormancyPeriodsInDays}
                          onChange={(e) => setstrMiddleName(e.target.value)}
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Last Name
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          defaultValue={data.strLastName}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Date Of Birth
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          id="strDOB"
                          defaultValue={data.strDOB}
                          onChange={(e) => setstrDOB(e.target.value)}
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Email Id
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          onChange={(e) => setstrEmailID(e.target.value)}
                          defaultValue={data.strEmailID}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Mobile Number
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          onChange={(e) => setstrMobileNo(e.target.value)}
                          defaultValue={data.strMobileNo}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Phone Number
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          onChange={(e) => setstrPhoneNo(e.target.value)}
                          defaultValue={data.strPhoneNo}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>

                    <div>
                      {""}
                      <h2 className="font-normal md:font-bold ">
                        Address Information
                      </h2>
                      {/* <br /> */}
                    </div>
                    <div className="grid gap-4 mb-2 md:grid-cols-3">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Address Line1
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          onChange={(e) => setstrAddress1(e.target.value)}
                          defaultValue={data.strAddress1}
                          id="strDormancyPeriodsInDays"
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Address Line2
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          onChange={(e) => setstrAddress2(e.target.value)}
                          defaultValue={data.strAddress2}
                          id="strAddress2"
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Address Line3
                        </label>
                        <input
                          type="text"
                          // value={strDormancyPeriodsInDays}
                          onChange={(e) => setstrAddress3(e.target.value)}
                          defaultValue={data.strAddress3}
                          id="strAddress3"
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Country<span className="text-red-600">*</span>
                        </label>
                        <select
                          name="strCountry"
                          id="strCountry"
                          value={strCountry}
                          onChange={(e) =>
                            setstrCountry(e.target.value, state(e.target.value))
                          }
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value="">Select</option>
                          {alldata3.map((data) => (
                            <option
                              className="capatlize text-sm"
                              value={data.countryName}
                              //onChange={setstrState(data.countryId)}
                            >
                              {data.countryName}
                            </option>
                          ))}
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          State<span className="text-red-600">*</span>
                        </label>
                        <select
                          id="strState"
                          name="strState"
                          value={strState}
                          onChange={(e) =>
                            setstrState(e.target.value, city(e.target.value))
                          }
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
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
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          City<span className="text-red-600">*</span>
                        </label>
                        <select
                          id="strCity"
                          name="strCity"
                          value={strCity}
                          onChange={(e) => setstrCity(e.target.value)}
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
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
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Postal Code
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAllowLoadCash}
                          defaultValue={data.strPinCode}
                          onChange={(e) => setstrPinCode(e.target.value)}
                          id="strPinCode"
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>

                    <div>
                      {""}
                      <h2 className="font-normal md:font-bold mb-1">
                        Address Proof
                      </h2>
                    </div>
                    <div className="grid gap-4 mb-2 md:grid-cols-2">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
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
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
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
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Document Value
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAllowLoadCash}
                          // defaultValue={data.strAllowLoadCash}
                          // onChange={(e) => setstrAllowLoadCash(e.target.value)}
                          id="strAllowLoadCash"
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>
                    <div className="grid gap-6 mb-6 md:grid-cols-2">
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Document Type
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAllowLoadCash}
                          // defaultValue={data.strAllowLoadCash}
                          // onChange={(e) => setstrAllowLoadCash(e.target.value)}
                          id="strAllowLoadCash"
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>

                      <div>
                        <label
                          htmlFor="text"
                          className="block text-sm font-semibold text-gray-700"
                        >
                          Document Value
                          <span className="text-red-600">*</span>
                        </label>
                        <input
                          type="text"
                          // value={strAllowLoadCash}
                          // defaultValue={data.strAllowLoadCash}
                          // onChange={(e) => setstrAllowLoadCash(e.target.value)}
                          id="strAllowLoadCash"
                          autoComplete="off"
                          className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>
                  </div>
                ))}
              </div>

              <div className="bg-gray-100 px-4 py-1 text-right sm:px-6 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  onClick={editAccountDetails}
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1 px-3 text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  // onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1 px-3 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                >
                  Clear
                </button>
                <button
                  title="Go Back"
                  type="button"
                  onClick={() => navigate(-1)}
                  className="text-white bg-blue-500 focus:outline-none focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-3 py-1  dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700"
                >
                  BACK
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
