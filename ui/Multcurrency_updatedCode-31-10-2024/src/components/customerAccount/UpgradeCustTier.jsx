import React, { useState, useEffect, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";
import { useNavigate, useParams } from "react-router-dom";
import amsApi from "../../api/amsApi";
import { Dialog, Transition } from "@headlessui/react";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";
export default function UpgradeCustTier() {
  const [title, setTitle] = useState("");
  const { strCustId } = useParams();
  const { strTierType } = useParams();
  const [reasion, setReasion] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

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

  const [list, setList] = useState({});
  let CustId = list.strCustId;
  let ActiveTier = list.strActiveTier;
  let FirstName = list.strFirstName;
  let LastName = list.strLastName;
  let Gender = list.strGender;
  let birthDate = list.birthDate;
  let EmailID = list.strEmailID;
  let MobileNo = list.strMobileNo;
  let Address1 = list.strAddress1;
  let PinCode = list.strPinCode;
  let Country = list.strCountry;
  let State = list.strState;
  let City = list.strCity;
  let PhoneCode = list.strPhoneCode;

  let Tier1PassportPhotograph = list.strTier1PassportPhotograph;
  let BvnNumber = list.strBvnNumber;
  let Tier2PassportPhotograph = list.strTier2PassportPhotograph;

  let AddressProofDocumentId = list.strAddressProofDocumentId;
  let AddressProofDocumentValue = list.strAddressProofDocumentValue;

  let IdentityProofDocumentId = list.strIdentityProofDocumentId;
  let IdentityProofDocumentValue = list.strIdentityProofDocumentValue;
  useEffect(() => {
    UpgardeTier();
  }, []);

  const UpgardeTier = async () => {
    if (!strCustId) {
      swal("Please check your strCustId");
    } else {
      try {
        const response = await amsApi.post(`customerId/getCustmerInfo`, {
          strCustId: strCustId,
        });
        if (response.data.code === "S0000") {
          setList(response.data.customerIdCreation);
          // showSuccess(response.data.message);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  let [isOpen, setIsOpen] = useState(false);
  function closeModal() {
    setIsOpen(false);
  }

  function openModal() {
    setIsOpen(true);
  }

  let [isOpen1, setIsOpen1] = useState(false);
  function closeModal1() {
    setIsOpen1(false);
  }

  function openModal1() {
    setIsOpen1(true);
  }

  let [isOpen2, setIsOpen2] = useState(false);
  function closeModal2() {
    setIsOpen2(false);
  }

  function openModal2() {
    setIsOpen2(true);
  }

  let [isOpen3, setIsOpen3] = useState(false);
  function closeModal3() {
    setIsOpen3(false);
  }

  function openModal3() {
    setIsOpen3(true);
  }

  // for reject

  let [isOpen4, setIsOpen4] = useState(false);
  function closeModal4() {
    setIsOpen4(false);
  }

  function openModal4() {
    setIsOpen4(true);
  }

  // For Aprrove

  const approve = async () => {
    try {
      const response = await amsApi.post(`approve_upgrade_tier/approveTir`, {
        // strRejectedReason: "-",
        strCustId: strCustId,
        strTierType: strTierType,
      });
      if (response.data.code === "S0000") {
        handleShowSuccess(response.data.message);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // reject
  const reject = async () => {
    if (reasion === "" || reasion.length === 0) {
      setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else {
      try {
        const response = await amsApi.post(`approve_upgrade_tier/rejectTir`, {
          strRejectedReason: reasion,
          strCustId: strCustId,
          strTierType: strTierType,
        });
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
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
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xl text-black :text-2xl  leading-normal ">
                Upgrade Customer {strTierType}
              </p>
            </div>
          </div>
        </div>
        <div className="overflow-hidden shadow ">
          <div className="grid  md:grid-cols-1 ">
            <div className=" px-4  bg-white ">
              <div className=" grid grid-cols-3  gap-6 px-8">
                <div className="grid2-item text-center">
                  {""}
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Customwe Id
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="strCustId"
                    defaultValue={CustId}
                    className="border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder="Enter Account number"
                  />
                </div>
                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Current Tier
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id=""
                    defaultValue={ActiveTier}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div className="mt-6">
                  <button
                    title="Go Back"
                    type="button"
                    onClick={() => navigate(-1)}
                    className="text-white bg-blue-500 focus:outline-none focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700"
                  >
                    BACK
                  </button>
                </div>
              </div>
            </div>
            <hr className="h-px my-1 bg-black border-0 dark:bg-gray-700" />

            {/*   Personal Information */}

            <div className=" px-4 py-2 bg-blue-200 ">
              <h1 className="font-medium flex justify-start  text-black text-2xl">
                Personal Information
              </h1>

              <div className=" grid lg:grid-cols-5 md:grid-cols-3 sm:grid-cols-1 gap-6 px-8 mt-4">
                <div className="grid2-item  text-center">
                  {""}
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Title
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <select
                    id="strTitle"
                    name="strTitle"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    required
                  >
                    <option value=""> Select </option>
                    <option value="Mr"> Mr.</option>
                    <option value="Mrs">Mrs.</option>
                  </select>
                </div>

                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    First Name
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    disabled
                    defaultValue={FirstName}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  />
                </div>

                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    middle Name
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="amount"
                    //   value={amount}
                    //   onChange={(e) => setAmount(e.target.value)}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Last Name
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="LastName"
                    defaultValue={LastName}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Gender
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="Gender"
                    defaultValue={Gender}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Date Of Birth
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    defaultValue={birthDate}
                    disabled
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  />
                </div>

                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Email Id
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="amount"
                    defaultValue={EmailID}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Country Code
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="amount"
                    defaultValue={PhoneCode}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>

                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Mobile Number
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="MobileNo"
                    defaultValue={MobileNo}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Phone Number
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="amount"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
              </div>
            </div>
            <hr className="h-px my-1 bg-black border-0 dark:bg-gray-700" />

            {/*   Address  Information */}
            <div className=" px-4 py-2 bg-blue-200 bg-gradi ">
              <h1 className="font-medium flex justify-start  text-black text-2xl">
                Address Information
              </h1>

              <div className=" grid lg:grid-cols-5 md:grid-cols-3 sm:grid-cols-1  gap-6 px-8 mt-4">
                <div className="grid2-item  text-center">
                  {""}
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Address Line 1<span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="Address1"
                    defaultValue={Address1}
                    className="border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder="Enter Account number"
                  />
                </div>

                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Address Line 2<span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    disabled
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  />
                </div>

                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Address Line 3<span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    id="amount"
                    //   value={amount}
                    //   onChange={(e) => setAmount(e.target.value)}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>

                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Country<span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    defaultValue={Country}
                    disabled
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  />
                </div>
                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    State<span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    defaultValue={State}
                    disabled
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  />
                </div>

                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    City<span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    defaultValue={City}
                    disabled
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  />
                </div>

                <div className="grid2-item  text-center">
                  <label
                    htmlFor="text"
                    className="flex justify-start text-md font-medium text-black"
                  >
                    Postal Code<span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="text"
                    defaultValue={PinCode}
                    disabled
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  />
                </div>

                {ActiveTier === "tier1" ||
                ActiveTier === "tier2" ||
                ActiveTier === "tier3" ? (
                  <div className="grid2-item  text-center lg:col-span-1 md:col-span-2">
                    <label
                      htmlFor="text"
                      className="flex justify-start text-md font-medium text-black"
                    >
                      Tier 1 :passport Photograph
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <button
                      title="Click Here View Image"
                      onClick={openModal}
                      className="bg-gray-50 border border-gray-300 text-blue-700 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    >
                      Click Here To View Image
                    </button>
                  </div>
                ) : (
                  ""
                )}

                {ActiveTier === "tier2" || ActiveTier === "tier3" ? (
                  <>
                    <div className="grid2-item  text-center">
                      <label
                        htmlFor="text"
                        className="flex justify-start text-md font-medium text-black"
                      >
                        BVN Number
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        defaultValue={BvnNumber}
                        disabled
                        className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                      />
                    </div>

                    <div className="grid2-item  text-center lg:col-span-1 md:col-span-2">
                      <label
                        htmlFor="text"
                        className="flex justify-start text-md font-medium text-black whitespace-nowrap"
                      >
                        Tier 2 :passport Photograph
                        <span className="text-red-600 px-2">*</span>
                      </label>

                      <button
                        title="Click Here View Image"
                        onClick={openModal1}
                        className="bg-gray-50 border border-gray-300 text-blue-700 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                      >
                        Click Here To View Image
                      </button>
                    </div>
                  </>
                ) : (
                  ""
                )}
              </div>
            </div>

            <hr className="h-px my-1 bg-black border-0 dark:bg-gray-700" />

            {/*Tier 3 address Proof(POA) */}
            {ActiveTier === "tier3" ? (
              <>
                <div className=" px-4 py-2 bg-blue-200 bg-gradi ">
                  <h1 className="font-medium flex justify-start  text-black text-2xl">
                    Address Proof(POA)
                  </h1>

                  <div className=" grid grid-cols-4  gap-6 px-8 mt-4">
                    <div className="grid2-item  text-center">
                      {""}
                      <label
                        htmlFor="text"
                        className="flex justify-start text-md font-medium text-black"
                      >
                        Document Type
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        id="AddressProofDocumentId"
                        defaultValue={AddressProofDocumentId}
                        disabled
                        className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                      />
                    </div>

                    <div className="grid2-item  text-center">
                      <label
                        htmlFor="text"
                        className="flex justify-start text-md font-medium text-black"
                      >
                        Document Value
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        defaultValue={AddressProofDocumentValue}
                        disabled
                        className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                      />
                    </div>

                    <div className="grid2-item  text-center">
                      <label
                        htmlFor="text"
                        className="flex justify-start text-md font-medium text-black"
                      >
                        Document Image
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <button
                        title="Click Here View Image"
                        onClick={openModal2}
                        className="bg-gray-50 border border-gray-300 text-blue-700 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                      >
                        Click Here To View Image
                      </button>
                    </div>
                  </div>
                </div>

                {/* Identification Proof */}
                <div className=" px-4 py-2 bg-blue-200 bg-gradi ">
                  <h1 className="font-medium flex justify-start  text-black text-2xl">
                    Identification Proof(POI)
                  </h1>

                  <div className=" grid grid-cols-4  gap-6 px-8 mt-4">
                    <div className="grid2-item  text-center">
                      {""}
                      <label
                        htmlFor="text"
                        className="flex justify-start text-md font-medium text-black"
                      >
                        Document type
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        id="IdentityProofDocumentId"
                        disabled
                        defaultValue={IdentityProofDocumentId}
                        className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                      />
                    </div>

                    <div className="grid2-item  text-center">
                      <label
                        htmlFor="text"
                        className="flex justify-start text-md font-medium text-black"
                      >
                        Document Value
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <input
                        type="text"
                        defaultValue={IdentityProofDocumentValue}
                        disabled
                        className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                      />
                    </div>

                    <div className="grid2-item  text-center">
                      <label
                        htmlFor="text"
                        className="flex justify-start text-md font-medium text-black"
                      >
                        Document Image
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <button
                        title="Clcik Here View Image"
                        onClick={openModal3}
                        className="bg-gray-50 border border-gray-300 text-blue-700 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                      >
                        Click here to View Image
                      </button>
                    </div>
                  </div>
                </div>
              </>
            ) : (
              ""
            )}
          </div>

          <div className="bg-gray-100 px-4 py-2 text-right sm:px-2 ">
            <button
              title="Click Approve Button"
              type="submit"
              onClick={approve}
              data-modal-toggle="defaultModal"
              className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2.5 px-5 mx-4 text-sm font-medium text-white shadow-sm"
            >
              Approve
            </button>
            <button
              title="Click Reject Button"
              type="button"
              onClick={openModal4}
              className="inline-flex justify-center rounded-md border border-transparent bg-red-400 py-2.5 px-5 mx-4 text-sm font-medium text-white shadow-sm hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2"
            >
              Reject
            </button>
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

          <Transition appear show={isOpen} as={Fragment}>
            <Dialog as="div" className="relative z-10" onClose={closeModal}>
              <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0"
                enterTo="opacity-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100"
                leaveTo="opacity-0"
              >
                <div className="fixed inset-0 bg-black bg-opacity-25" />
              </Transition.Child>

              <div className="fixed inset-0 overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4 text-center">
                  <Transition.Child
                    as={Fragment}
                    enter="ease-out duration-300"
                    enterFrom="opacity-0 scale-95"
                    enterTo="opacity-100 scale-100"
                    leave="ease-in duration-200"
                    leaveFrom="opacity-100 scale-100"
                    leaveTo="opacity-0 scale-95"
                  >
                    <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                      <Dialog.Title
                        as="h3"
                        className="text-lg font-medium leading-6 text-blue-900"
                      >
                        View Passport Photograph
                      </Dialog.Title>
                      <div className="mt-4">
                        <img
                          src={Tier1PassportPhotograph}
                          alt="Tier 1 Passport Photograph"
                          className="max-w-full h-auto"
                        />
                      </div>

                      <div className="mt-4 flex space-x-2 justify-end items-end sm:px-2">
                        <button
                          title="Cancel Here "
                          type="button"
                          className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2.5 px-5 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                          onClick={closeModal}
                        >
                          Cancel
                        </button>
                      </div>
                    </Dialog.Panel>
                  </Transition.Child>
                </div>
              </div>
            </Dialog>
          </Transition>

          {/* for Tier 2 */}
          <Transition appear show={isOpen1} as={Fragment}>
            <Dialog as="div" className="relative z-10" onClose={closeModal1}>
              <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0"
                enterTo="opacity-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100"
                leaveTo="opacity-0"
              >
                <div className="fixed inset-0 bg-black bg-opacity-25" />
              </Transition.Child>

              <div className="fixed inset-0 overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4 text-center">
                  <Transition.Child
                    as={Fragment}
                    enter="ease-out duration-300"
                    enterFrom="opacity-0 scale-95"
                    enterTo="opacity-100 scale-100"
                    leave="ease-in duration-200"
                    leaveFrom="opacity-100 scale-100"
                    leaveTo="opacity-0 scale-95"
                  >
                    <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                      <Dialog.Title
                        as="h3"
                        className="text-lg font-medium leading-6 text-blue-900"
                      >
                        View Passport Photograph
                      </Dialog.Title>
                      <div className="mt-4">
                        <img
                          src={Tier2PassportPhotograph}
                          alt="Tier 2 Passport Photograph"
                          className="max-w-full h-auto"
                        />
                      </div>

                      <div className="mt-4 flex space-x-2 justify-end items-end sm:px-2">
                        <button
                          title="Cancel Here"
                          type="button"
                          className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2.5 px-5 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                          onClick={closeModal1}
                        >
                          Cancel
                        </button>
                      </div>
                    </Dialog.Panel>
                  </Transition.Child>
                </div>
              </div>
            </Dialog>
          </Transition>

          {/*for tier 3   poa*/}
          <Transition appear show={isOpen2} as={Fragment}>
            <Dialog as="div" className="relative z-10" onClose={closeModal2}>
              <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0"
                enterTo="opacity-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100"
                leaveTo="opacity-0"
              >
                <div className="fixed inset-0 bg-black bg-opacity-25" />
              </Transition.Child>

              <div className="fixed inset-0 overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4 text-center">
                  <Transition.Child
                    as={Fragment}
                    enter="ease-out duration-300"
                    enterFrom="opacity-0 scale-95"
                    enterTo="opacity-100 scale-100"
                    leave="ease-in duration-200"
                    leaveFrom="opacity-100 scale-100"
                    leaveTo="opacity-0 scale-95"
                  >
                    <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                      <Dialog.Title
                        as="h3"
                        className="text-lg font-medium leading-6 text-blue-900"
                      >
                        View Passport Photograph
                      </Dialog.Title>
                      <div className="mt-4">
                        <img
                          src={Tier1PassportPhotograph}
                          alt="Tier 1 Passport Photograph"
                          className="max-w-full h-auto"
                        />
                      </div>

                      <div className="mt-4 flex space-x-2 justify-end items-end sm:px-2">
                        <button
                          title="Cancel Here"
                          type="button"
                          className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2.5 px-5 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                          onClick={closeModal2}
                        >
                          Cancel
                        </button>
                      </div>
                    </Dialog.Panel>
                  </Transition.Child>
                </div>
              </div>
            </Dialog>
          </Transition>
          {/* for tie3 poi */}
          <Transition appear show={isOpen3} as={Fragment}>
            <Dialog as="div" className="relative z-10" onClose={closeModal3}>
              <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0"
                enterTo="opacity-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100"
                leaveTo="opacity-0"
              >
                <div className="fixed inset-0 bg-black bg-opacity-25" />
              </Transition.Child>

              <div className="fixed inset-0 overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4 text-center">
                  <Transition.Child
                    as={Fragment}
                    enter="ease-out duration-300"
                    enterFrom="opacity-0 scale-95"
                    enterTo="opacity-100 scale-100"
                    leave="ease-in duration-200"
                    leaveFrom="opacity-100 scale-100"
                    leaveTo="opacity-0 scale-95"
                  >
                    <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                      <Dialog.Title
                        as="h3"
                        className="text-lg font-medium leading-6 text-blue-900"
                      >
                        View Passport Photograph
                      </Dialog.Title>
                      <div className="mt-4">
                        <img
                          src={Tier1PassportPhotograph}
                          alt="Tier 1 Passport Photograph"
                          className="max-w-full h-auto"
                        />
                      </div>

                      <div className="mt-4 flex space-x-2 justify-end items-end sm:px-2">
                        <button
                          title="Cancel Here"
                          type="button"
                          className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2.5 px-5 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                          onClick={closeModal3}
                        >
                          Cancel
                        </button>
                      </div>
                    </Dialog.Panel>
                  </Transition.Child>
                </div>
              </div>
            </Dialog>
          </Transition>

          {/*for  Reject   */}
          <Transition appear show={isOpen4} as={Fragment}>
            <Dialog as="div" className="relative z-10" onClose={closeModal4}>
              <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0"
                enterTo="opacity-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100"
                leaveTo="opacity-0"
              >
                <div className="fixed inset-0 bg-black bg-opacity-25" />
              </Transition.Child>

              <div className="fixed inset-0 overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4 text-center">
                  <Transition.Child
                    as={Fragment}
                    enter="ease-out duration-300"
                    enterFrom="opacity-0 scale-95"
                    enterTo="opacity-100 scale-100"
                    leave="ease-in duration-200"
                    leaveFrom="opacity-100 scale-100"
                    leaveTo="opacity-0 scale-95"
                  >
                    <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                      <Dialog.Title
                        as="h3"
                        className="text-lg font-medium leading-6 text-blue-900"
                      >
                        Reason For Reject :
                      </Dialog.Title>
                      <div className="mt-4">
                        <textarea
                          type="text"
                          value={reasion}
                          onChange={(e) => setReasion(e.target.value)}
                          className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-4 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                        />
                        {error && reasion.length <= 0 ? (
                          <p className="text-red-500   text-sm font-medium">
                            Please Enter Reasion
                          </p>
                        ) : (
                          "-"
                        )}
                      </div>

                      <div className="mt-4 flex space-x-2 justify-end items-end sm:px-2">
                        <button
                          title="Click Submit Button"
                          type="button"
                          className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2.5 px-5 mx-4 text-sm font-medium text-white shadow-sm"
                          onClick={reject}
                        >
                          Submit
                        </button>
                        <button
                          title="Cancel Here"
                          type="button"
                          className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2.5 px-5 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                          onClick={closeModal4}
                        >
                          Cancel
                        </button>
                      </div>
                    </Dialog.Panel>
                  </Transition.Child>
                </div>
              </div>
            </Dialog>
          </Transition>
          {/* ))} */}
        </div>
      </div>
    </AppLayout>
  );
}
